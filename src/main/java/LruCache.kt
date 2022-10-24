import LruCacheException.ExceptionType.*
import java.util.*

class LruCache<K, V>(cacheSize: Int = 100) {

    var cacheSize: Int = cacheSize
        private set

    private val cachedValuesQueue: LinkedList<K> = LinkedList()
    private val cachedValuesMap: MutableMap<K, V> = HashMap()

    fun get(key: K): V? {
        return if (cachedValuesMap.containsKey(key)) {
            cachedValuesMap[key]
        } else {
            null
        }
    }

    fun put(key: K, value: V): Boolean {
        if (cachedValuesMap.contains(key)) {
            cachedValuesMap[key] = value
            return false
        }

        assert(cachedValuesMap.size <= cacheSize) { throwException(OVERFLOW) }
        assert(cachedValuesMap.size == cachedValuesQueue.size) { throwException(INCONSISTENT_STATE) }

        if (cachedValuesMap.size == cacheSize) {
            removeLeastUsed()
        }

        cachedValuesMap[key] = value
        cachedValuesQueue.addFirst(key)
        return true
    }

    fun emptyCache() {
        cachedValuesMap.clear()
        cachedValuesQueue.clear()
    }

    fun changeCapacity(newCapacity: Int) {
        assert(newCapacity > 0) { throwException(ILLEGAL_ARGUMENT) }
        assert(cachedValuesMap.size == cachedValuesQueue.size) { throwException(INCONSISTENT_STATE) }

        while (cachedValuesMap.size > newCapacity) {
            removeLeastUsed()
        }
        cacheSize = newCapacity
    }

    private fun removeLeastUsed() {
        assert(cachedValuesQueue.isNotEmpty()) { throwException(ILLEGAL_OPERATION) }

        val leastUsedValue = cachedValuesQueue.last
        assert(cachedValuesMap.contains(leastUsedValue)) { throwException(INCONSISTENT_STATE) }

        cachedValuesMap.remove(leastUsedValue)
        cachedValuesQueue.removeLast()
    }

    private fun throwException(exception: LruCacheException.ExceptionType) {
        LruCacheException(exception.message).throwThis()
    }
}