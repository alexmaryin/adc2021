import kotlin.math.abs
import kotlin.math.min

fun main() {
    fun part1(input: List<Int>, stepCallback: (Int) -> Int = { it }): Int {
        val sorted = input.sorted()
        var minFuel: Int? = null
        for (i in sorted.first()..sorted.last()) {
            val fuel = sorted.fold(0) { fuel, pos ->
                fuel + stepCallback(abs(i - pos))
            }
            minFuel = if (minFuel == null) {
                fuel
            } else {
                min(minFuel, fuel)
            }
        }
        return minFuel ?: throw RuntimeException("Something wrong")
    }

    fun sequenceSum(value: Int) = (value * (value + 1)) / 2

    fun part2(input: List<Int>): Int {
        return part1(input) { sequenceSum(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")[0].split(",").map { it.toInt() }
    runMeasuredTime {
        check(part1(testInput) == 37)
    }
    runMeasuredTime {
        check(part2(testInput) == 168)
    }

    val input = readInput("Day07")[0].split(",").map { it.toInt() }
    runMeasuredTime {
        println(part1(input))
    }
    runMeasuredTime {
        println(part2(input))
    }

}