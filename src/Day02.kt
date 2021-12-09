@file:Suppress("KotlinConstantConditions")

fun main() {
    fun part1(input: List<String>): Int {
        var distance = 0
        var depth = 0
        input.forEach { line ->
            val (command, value) = line.split(" ", limit = 2)
            when (command) {
                "forward" -> distance += value.toInt()
                "down" -> depth += value.toInt()
                "up" -> depth = (depth - value.toInt()).coerceAtLeast(0)
            }
        }
        return distance * depth
    }

    fun part2(input: List<String>): Int {
        var distance = 0
        var depth = 0
        var aim = 0
        input.forEach { line ->
            val (command, value) = line.split(" ", limit = 2)
            when (command) {
                "up" -> aim = (aim - value.toInt()).coerceAtLeast(0)
                "down" -> aim += value.toInt()
                "forward" -> {
                    distance += value.toInt()
                    depth += (aim * value.toInt())
                }
            }
        }
        return distance * depth
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    runMeasuredTime {
        println(part1(input))
    }
    runMeasuredTime {
        println(part2(input))
    }
}