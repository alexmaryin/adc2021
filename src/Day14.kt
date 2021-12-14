fun main() {
    fun part1(input: List<String>, steps: Int): Int {

        fun afterStep(origin: String, map: Map<String, String>) =
            origin.windowed(2) { pair ->
                val middle = map[pair]
                "${pair[0]}$middle"
            }.joinToString("") + origin.last()

        val (origin, pairs) = input.parseToMap()

        val result = (1..steps).fold(origin) { lines, _ ->
            afterStep(lines, pairs)
        }

        val (maxCount, minCount) = with(result.groupBy { it }) {
            maxOf { it.value.size } to minOf { it.value.size }
        }
        return maxCount - minCount
    }

    fun part2(input: List<String>, steps: Int): Long {

        fun processSteps(
            before: Map<String, Long>,
            map: Map<String, String>,
            steps: Int
        ): Map<String, Long> = (1..steps).fold(before) { poly, _ ->
            buildMap {
                poly.forEach { (item, count) ->
                    val left = "${item[0]}${map[item]}"
                    val right = "${map[item]}${item[1]}"
                    put(left, getOrDefault(left, 0) + count)
                    put(right, getOrDefault(right, 0) + count)
                }
            }
        }

        val (origin, pairs) = input.parseToMap()
        val originMap = origin.windowed(2).groupingBy { it }.eachCount().mapValues { it.value.toLong() }

        val result = processSteps(originMap, pairs, steps)

        val chars: Map<Char, Long> = buildMap {
            put(origin[0], 1L)
            result.forEach { (poly, count) ->
                put(poly[1], getOrDefault(poly[1], 0L) + count)
            }
        }

        return chars.maxOf { it.value } - chars.minOf { it.value }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day14_test")
    check(part1(testInput, 10) == 1588)
    check(part2(testInput, 40) == 2188189693529)

    val input = readInput("Day14")
    runMeasuredTime {
        println(part1(input, 10))
    }
    runMeasuredTime {
        println(part2(input, 40))
    }
}

fun List<String>.parseToMap(): Pair<String, Map<String, String>> {
    val (pairs, origin) = partition { it.contains("->") }
    val list = pairs.filterNot { it.isBlank() }.map {
        val (left, right) = it.split(" -> ", limit = 2)
        left to right
    }
    return origin.first() to list.toMap()
}

