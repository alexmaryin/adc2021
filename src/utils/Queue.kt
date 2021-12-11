package utils

import java.util.*

open class Queue<T> {
    private val items = LinkedList<T>()

    open fun insert(item: T): Boolean {
        items.addLast(item)
        return true
    }

    protected fun notContains(item: T) = items.contains(item).not()

    fun next(): T? = if(items.size > 0) items.removeFirst() else null

    fun isNotEmpty() = items.isEmpty().not()
}


