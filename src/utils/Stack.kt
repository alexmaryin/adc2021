package utils

class Stack<T> {
    private val items = mutableListOf<T>()

    fun push(item: T) {
        items.add(0, item)
    }

    fun pop(): T? = items.removeFirstOrNull()

    fun peek(): T? = if (items.isNotEmpty()) items[0] else null

    fun size(): Int = items.size
}