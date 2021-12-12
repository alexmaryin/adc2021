package utils

data class Leaf<T>(
    val item: T,
    val children: MutableList<Leaf<T>> = mutableListOf()
) {
    override fun equals(other: Any?) = other is Leaf<*> && item == other.item
    override fun hashCode(): Int = item.hashCode()
}

class Graph<T> {
    private val items = ArrayDeque<Leaf<T>>()

    fun insert(item: T, parent: T? = null, addOnTop: Boolean = false) {

        var leaf = Leaf(item)

        if (items.contains(leaf).not()) {
            if (addOnTop) items.addFirst(leaf) else items.addLast(leaf)
        } else {
            leaf = items.find { it.item == item }!!
        }

        parent?.let { root ->
            items.find { it.item == root }?.children?.add(leaf)
        }
    }

    fun root() = items.first()

    fun parent(leaf: Leaf<T>) = items.find { it.children.contains(leaf) }

    override fun toString(): String = buildString {
        items.forEach { append("${it.item} -> ${it.children}\n") }
    }
}