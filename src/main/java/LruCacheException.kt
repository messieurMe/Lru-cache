class LruCacheException(override val message: String) : RuntimeException() {

    fun throwThis(): Nothing = throw this

    enum class ExceptionType(val message: String) {
        INCONSISTENT_STATE("Inconsistent state of cache"),
        OVERFLOW("Cache overflow occurred"),
        ILLEGAL_ARGUMENT("Illegal argument were given"),
        ILLEGAL_OPERATION("Cached tried to perform illegal operation")
    }
}