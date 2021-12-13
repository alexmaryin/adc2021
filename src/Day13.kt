import utils.Point

fun main() {
    fun part1(input: List<String>, onlyFirst: Boolean): Set<Point> {
        val (folds, points) = input.buildField()
        val steps = if (onlyFirst) folds.take(1) else folds
        val finished = steps.fold(points) { field, step ->
            when(step.axis) {
                'x' -> field.map { it.foldLeft(step.index) }.toSet()
                'y' -> field.map { it.foldUp(step.index) }.toSet()
                else -> throw RuntimeException("Unknown step axis")
            }
        }
        return finished
    }

    fun part2(input: List<String>) {
        val result = part1(input, onlyFirst = false)
        printField(result)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day13_test")
    check(part1(testInput, onlyFirst = true).count() == 17)

    val input = readInput("Day13")
    runMeasuredTime {
        println(part1(input, onlyFirst = true).count())
    }

    runMeasuredTime {
        part2(input)
    }
}

fun printField(points: Set<Point>) {
    val width = points.maxOf { it.x }
    val height = points.maxOf { it.y }
    repeat(height + 1) { y ->
        repeat(width + 1) { x ->
            print(if (points.contains(Point(x, y))) "▓" else " ")
        }
        print("\n")
    }
}

fun List<String>.buildField(): Pair<List<FoldStep>, Set<Point>> {
    val (folds, points) = partition { it.contains("fold along") }
    val pointsList = points.filter { it.isNotBlank() }.map {
        val (x, y) = it.split(",", limit = 2)
        Point(x.toInt(), y.toInt())
    }.toSet()

    val foldSteps = folds.map {
        val (axis, index) = it.split("=")
        FoldStep(axis.last(),index.toInt())
    }
    return foldSteps to pointsList
}

data class FoldStep(val axis: Char, val index: Int)

fun Point.foldUp(axisY: Int) = if (y > axisY) Point(x, axisY - (y - axisY)) else this

fun Point.foldLeft(axisX: Int) = if (x > axisX) Point(axisX - (x - axisX), y) else this
