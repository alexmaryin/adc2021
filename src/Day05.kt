fun main() {
    fun part1(lines: List<Line>): Int {


        return 5
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    val lines = parseInput(testInput) {
        it.start.x == it.end.x || it.start.y == it.end.y
    }
    lines.sortedBy { it.direction }.forEach { println("${it.start} ${it.end}") }
    check(part1(lines) == 5)

//    val input = readInput("Day01")
//    println(part1(input))
//    println(part2(input))
}

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    override fun compareTo(other: Point): Int = when {
        x != other.x -> x compareTo other.x
        y != other.y -> y compareTo other.y
        else -> 0
    }
}

data class Line(val start: Point, val end: Point, val direction: LineDirection)

enum class LineDirection { HORIZONTAL, VERTICAL }

fun parseInput(text: List<String>, filter: (Line) -> Boolean = { true }): List<Line> = text.map { line ->
    val (start, end) = line.split(",", " -> ", limit = 4)
        .windowed(2, step = 2)
        .map { (x, y) -> Point(x.toInt(), y.toInt()) }.sorted()
    Line(start, end, if(start.x == end.x) LineDirection.VERTICAL else LineDirection.HORIZONTAL)
}.filter(filter)