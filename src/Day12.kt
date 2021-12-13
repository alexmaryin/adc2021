import kotlin.contracts.contract

fun main() {
    fun part1(input: List<String>, allowTwice: Boolean = false): Int {
        val graph = input.buildGraph()
        println(graph)
        return trace("start", graph, allowTwice)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day12_test1")
    check(part1(testInput) == 10)
    check(part1(testInput, allowTwice = true) == 36)

    val input = readInput("Day12")
    runMeasuredTime {
        println(part1(input, allowTwice = false))
    }
    runMeasuredTime {
        println(part1(input, allowTwice = true))
    }
}

fun List<String>.buildGraph(): Map<String, Set<String>> {
    val map = mutableMapOf<String, Set<String>>()
    forEach {
        val (left, right) = it.split("-", limit = 2)
        val leftSet = map[left]?.toMutableSet() ?: mutableSetOf()
        leftSet.add(right)
        map[left] = leftSet.toSet()
        if (left != "start") {
            val rightSet = map[right]?.toMutableSet() ?: mutableSetOf()
            rightSet.add(left)
            map[right] = rightSet.toSet()
        }
    }
    return map.toMap()
}

data class Path(
    val route: MutableList<String>,
    val caves: MutableSet<String> = mutableSetOf(),
    val hasDouble: Boolean = false
)

fun trace(initial: String, graph: Map<String, Set<String>>, allowTwice: Boolean = false): Int {
    var count = 0
    val paths = mutableListOf(Path(mutableListOf(initial)))

    while (paths.isNotEmpty()) {
        val nextPath = paths.removeFirst()
        graph[nextPath.route.last()]?.forEach { child ->
            when {
                child == "end" -> {
//                    nextPath.route += child
                    count++
//                    println("$count: ${nextPath.route}")
                }
                child.isLowerCase() -> {
                    if (child !in nextPath.caves)
                        paths.add(
                            Path(
                                (nextPath.route + child).toMutableList(),
                                (nextPath.caves + child).toMutableSet(),
                                nextPath.hasDouble
                            )
                        )
                    else if (allowTwice && nextPath.hasDouble.not())
                        paths.add(
                            Path(
                                (nextPath.route + child).toMutableList(),
                                (nextPath.caves + child).toMutableSet(),
                                true
                            )
                        )
                }
                else -> paths.add(
                    Path(
                        (nextPath.route + child).toMutableList(),
                        nextPath.caves,
                        nextPath.hasDouble
                    )
                )
            }
        }
    }
    return count
}

fun String.isLowerCase() = this == lowercase()