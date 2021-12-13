import utils.Line
import utils.LineDirection
import utils.Point

fun main() {
    fun part1(size: Int, lines: List<Line>): Int {
        return processLines(size, lines)
    }

    // test if implementation meets criteria from the description, like:
    runMeasuredTime {
        val testInput = readInput("Day05_test")
        val testLines = parseInput(testInput)
        check(part1(10, testLines) == 12)
    }

    runMeasuredTime {
        println("Only horizontal and vertical lines")
        val input = readInput("Day05")
        val lines = parseInput(input) { it.start.x == it.end.x || it.start.y == it.end.y }
        println(part1(1000, lines))
    }

    runMeasuredTime {
        println("All lines including diagonals")
        val input = readInput("Day05")
        val lines = parseInput(input)
        println(part1(1000, lines))
    }
}

fun parseInput(text: List<String>, filter: (Line) -> Boolean = { true }): List<Line> = text.map { line ->
    val (start, end) = line.split(",", " -> ", limit = 4)
        .windowed(2, step = 2)
        .map { (x, y) -> Point(x.toInt(), y.toInt()) }.sorted()
    Line(
        start = start,
        end = end,
        direction = when {
            start.x == end.x -> LineDirection.VERTICAL
            start.y == end.y -> LineDirection.HORIZONTAL
            start.x < end.x && start.y < end.y -> LineDirection.LEFT_DIAGONAL
            else -> LineDirection.RIGHT_DIAGONAL
        }
    )

}.filter(filter)

fun processLines(size: Int, lines: List<Line>): Int {
    val field = List(size) { MutableList(size) { 0 } }
    lines.forEach { line ->
        when (line.direction) {
            LineDirection.HORIZONTAL -> {
                for (x in line.start.x..line.end.x) {
                    field[line.start.y][x] += 1
                }
            }
            LineDirection.VERTICAL -> {
                for (y in line.start.y..line.end.y) {
                    field[y][line.start.x] += 1
                }
            }
            LineDirection.LEFT_DIAGONAL -> {
                var y = line.start.y
                for (x in line.start.x..line.end.x) {
                    field[y][x] += 1
                    y++
                }
            }
            LineDirection.RIGHT_DIAGONAL -> {
                var y = line.start.y
                for (x in line.start.x..line.end.x) {
                    field[y][x] += 1
                    y--
                }
            }
        }
    }
//    field.forEach {
//        println(it.joinToString("") { num -> if(num > 0) num.toString() else "." })
//    }
    return field.sumOf { row -> row.count { it >= 2 } }
}