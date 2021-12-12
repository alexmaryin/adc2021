import utils.Graph
import utils.Leaf

fun main() {
    fun part1(input: List<String>): Int {
        val graph = input.buildGraph()
        println(graph)

        val g = graph.traceGraph(graph.root(), 0)
        println(g)
        return g
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test1")
    check(part1(testInput) == 1)

//    val input = readInput("Day12")
//    println(part1(input))
//    println(part2(input))
}

fun List<String>.buildGraph() = Graph<CaveItem>().apply {
    forEach { line ->
        val (parent, child) = line.split("-", limit = 2)
            .map {
                when {
                    it == "start" -> CaveItem(it, CaveType.START)
                    it == "end" -> CaveItem(it, CaveType.END)
                    it.all { letter -> letter.isUpperCase() } -> CaveItem(it, CaveType.BIG)
                    else -> CaveItem(it, CaveType.SMALL)
                }
            }.sorted()

        insert(parent, addOnTop = parent.type == CaveType.START)
        insert(child, parent = parent)
    }
}

data class CaveItem(
    val name: String,
    val type: CaveType
) : Comparable<CaveItem> {

    override fun compareTo(other: CaveItem): Int = when (other.type) {
        CaveType.START -> 1
        CaveType.END -> -1
        else -> name compareTo other.name
    }

    override fun toString() = name
}

enum class CaveType {
    START, BIG, SMALL, END
}

fun Graph<CaveItem>.traceGraph(leaf: Leaf<CaveItem>, routes: Int, visited: MutableList<CaveItem> = mutableListOf()): Int {
    print("${leaf.item.name}-")
    if (leaf.item.type == CaveType.END) {
        print(" finished, total ${routes+1}\n")
        return routes + 1
    }

    if (leaf.item.type == CaveType.SMALL && parent(leaf)?.item?.type == CaveType.SMALL) {
        print(" bad route, total finished $routes\n")
        return routes
    }

    if(leaf.item.type == CaveType.SMALL) {
        visited += leaf.item
    }

    if (leaf.children.isEmpty()) {
        traceGraph(parent(leaf)!!, routes, visited)
    }

    var stepRoutes = 0
    leaf.children.forEach {
        if (visited.contains(it.item).not()) stepRoutes += traceGraph(it, stepRoutes, visited)
    }
    return routes + stepRoutes
}