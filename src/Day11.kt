import utils.convertToMatrix

fun main() {
    fun part1(field: List<MutableList<Int>>, runPredicate: (Int) -> Boolean): Int {

        var flashCount = 0
        val flashedAtStep = mutableListOf<Pair<Int, Int>>()

        fun flashAt(row: Int, col: Int) {
            flashCount++
            field[row][col] = 0
            flashedAtStep += row to col
            val adjacentRows = listOf(-1, -1, -1, 0, 0, 1, 1, 1)
            val adjacentCols = listOf(-1, 0, 1, -1, 1, -1, 0, 1)
            repeat(8) {
                val adjRow = row + adjacentRows[it]
                val adjCol = col + adjacentCols[it]
                if (adjRow in field.indices && adjCol in 0 until field[0].size) {
                    if (flashedAtStep.contains(adjRow to adjCol).not()) {
                        val adjPower = ++field[adjRow][adjCol]
                        if (adjPower > 9) flashAt(adjRow, adjCol)
                    }
                }
            }
        }

        var step = 0
        while (runPredicate(step)) {
//            println("Step $step")
//            field.forEach { println(it.joinToString("")) }

            for (row in field.indices)
                for (col in 0 until field[0].size) {
                    if (flashedAtStep.contains(row to col).not()) {
                        val current = ++field[row][col]
                        if (current > 9) flashAt(row, col)
                    }
                }
            flashedAtStep.clear()
            step++
        }

        return flashCount
    }

    fun part2(field: List<MutableList<Int>>): Int {
        var step = 0
        part1(field) {
            step = it
            var next = false
            for (row in field.indices)
                for (col in 0 until field[0].size) {
                    if (field[row][col] != 0) next = true
                }
            next
        }
        return step
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day11_test")
    runMeasuredTime {
        check(part1(testInput.convertToMatrix()) { it < 100 } == 1656)
        check(part2(testInput.convertToMatrix()) == 195)
    }

    val input = readInput("Day11")
    runMeasuredTime {
        println(part1(input.convertToMatrix()) { it < 100 })
    }
    runMeasuredTime {
        println(part2(input.convertToMatrix()))
    }
}