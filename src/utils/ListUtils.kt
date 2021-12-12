package utils

fun <T> List<T>.middleItem(): T? = if(size % 2 != 0) this[size / 2] else null

fun List<String>.convertToMatrix() = buildList {
    this@convertToMatrix.forEach { row ->
        add(row.map { it.digitToInt() }.toMutableList())
    }
}