import utils.QueueSet
import utils.convertToMatrix

fun main() {

    fun growField(digits: List<List<Int>>) = buildList {
        add(List(digits[0].size + 2) { 9 })
        digits.forEach {
            val line = mutableListOf(9)
            line.addAll(it)
            line.add(9)
            add(line)
        }
        add(List(digits[0].size + 2) { 9 })
    }

    fun scanField(field: List<List<Int>>, block: (Int, Int, Int) -> Unit) {
        for (row in 1 until field.size - 1) {
            for (col in 1 until field[0].size - 1) {
                val cell = field[row][col]
                val adjacent = listOf(
                    field[row - 1][col],
                    field[row][col - 1], field[row][col + 1],
                    field[row + 1][col]
                )
                if (adjacent.all { it > cell }) {
                    block(row, col, cell)
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val digits = input.convertToMatrix()
        val field = growField(digits)

        var sum = 0
        scanField(field) { _, _, value ->
            sum += value + 1
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val digits = input.convertToMatrix()
        val field = growField(digits)

        val lowPoints = mutableListOf<LowPoint>()
        scanField(field) { row, col, value ->
            lowPoints += LowPoint(row, col, value)
        }

        val basinList = mutableListOf<Int>()
        lowPoints.forEach {
            var basin = 1
            val pointToVisit = QueueSet<LowPoint>()
            val visited = mutableListOf<LowPoint>()
            pointToVisit.insert(it)

            while (pointToVisit.isNotEmpty()) {
                val (row, col, value) = pointToVisit.next()!!
                val up = LowPoint(row - 1, col, field[row - 1][col])
                val down = LowPoint(row + 1, col, field[row + 1][col])
                val left = LowPoint(row, col - 1, field[row][col - 1])
                val right = LowPoint(row, col + 1, field[row][col + 1])

                if (up.value in value + 1..8 && visited.contains(up).not()) {
                    if (pointToVisit.insert(up)) basin++
                }

                if (down.value in value + 1..8 && visited.contains(down).not()) {
                    if (pointToVisit.insert(down)) basin++
                }

                if (left.value in value + 1..8 && visited.contains(left).not()){
                    if (pointToVisit.insert(left)) basin++
                }

                if (right.value in value + 1..8 && visited.contains(right).not()){
                    if (pointToVisit.insert(right)) basin++
                }
                visited += LowPoint(row, col, value)
            }
            basinList += basin
        }
        return basinList.sortedDescending().take(3).reduce { acc, next -> acc * next }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    check(part1(testInput) == 15)
    check(part2(testInput) == 1134)

    val input = readInput("Day09")
    runMeasuredTime {
        println(part1(input))
    }
    runMeasuredTime {
        println(part2(input))
    }
}

data class LowPoint(val row: Int, val col: Int, val value: Int)

