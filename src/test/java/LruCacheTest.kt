import LruCacheException.ExceptionType.ILLEGAL_ARGUMENT
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class LruCacheTest {

    private val lruCache = LruCache<Int, Int>()
    private val defaultKey = 1
    private val defaultValue = 2


    @AfterEach
    fun emptyCache() {
        lruCache.emptyCache()
    }

    @Test
    fun `single add`() {
        lruCache.put(defaultKey, defaultValue)
        assert(lruCache.get(defaultKey) == defaultValue)
    }

    @Test
    fun `cache removed correct value`() {
        lruCache.changeCapacity(1)

        lruCache.put(defaultKey, defaultValue)
        lruCache.put(-1, -2)

        assert(lruCache.get(defaultKey) == null)
        assert(lruCache.get(-1) == -2)
    }

    @Test
    fun `correct data after changing capacity`() {
        val initialCapacity = 10
        val newCapacity = 5

        val keys = IntArray(initialCapacity) { it }
        val values = IntArray(initialCapacity) { initialCapacity - it }

        for (i in 0 until initialCapacity) {
            lruCache.put(keys[i], values[i])
        }

        for (i in 0 until initialCapacity) {
            assert(lruCache.get(keys[i]) == values[i])
        }

        lruCache.changeCapacity(newCapacity)

        for (i in 0 until newCapacity) {
            assert(lruCache.get(keys[i]) == null)
        }
        for (i in newCapacity until initialCapacity) {
            assert(lruCache.get(keys[i]) == values[i])
        }
    }

    @Test
    fun `attempts to change capacity on different numbers`() {
        catchException<LruCacheException>(ILLEGAL_ARGUMENT.message) {
            lruCache.changeCapacity(newCapacity = -1)
        }
        catchException<LruCacheException>(ILLEGAL_ARGUMENT.message) {
            lruCache.changeCapacity(newCapacity = 0)
        }
        lruCache.changeCapacity(newCapacity = 1)
        lruCache.changeCapacity(newCapacity = 10)
        lruCache.changeCapacity(newCapacity = 10_000)
        catchException<LruCacheException>(ILLEGAL_ARGUMENT.message) {
            lruCache.changeCapacity(newCapacity = -1)
        }

    }

    private inline fun <reified E : Exception> catchException(message: String, block: () -> Unit) {
        try {
            block()
        } catch (e: Exception) {
            assert(e is E)
            assert(e.message == message)
        }
    }
}