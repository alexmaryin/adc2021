package utils

class QueueSet<T> : Queue<T>() {
    override fun insert(item: T): Boolean =
        if (notContains(item)) super.insert(item) else false
}