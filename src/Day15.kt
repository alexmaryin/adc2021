import utils.Point
import java.util.*

fun main() {
    fun part1(input: List<String>, mult: Int = 1): Int {
        val field = if (mult > 1) input.parse().parseToFull(mult) else input.parse()
//        printField(field)
        val finish = Point(field.maxOf { it.key.x }, field.maxOf { it.key.y })

        val queue = PriorityQueue<QPoint>()
        val riskMap = mutableMapOf(Point(0, 0) to 0)
        queue.add(QPoint(0, 0, 0))

        while (queue.isNotEmpty()) {
            val next = queue.remove()
            val adjacent = listOf(
                Point(next.x, next.y - 1),
                Point(next.x - 1, next.y),
                Point(next.x, next.y + 1),
                Point(next.x + 1, next.y),
            )
            adjacent.forEach { adj ->
                if(field.containsKey(adj) && riskMap.containsKey(adj).not()) {
                    val risk = riskMap[next.xy()]!! + field[adj]!!
                    riskMap[adj] = risk
                    if (adj == finish)
                        return@forEach
                    queue.add(QPoint(adj.x, adj.y, risk))
                }
            }
        }

        return riskMap[finish]!!
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day15_test")
    check(part1(testInput) == 40)
    check(part1(testInput, 5) == 315)

    val input = readInput("Day15")
    runMeasuredTime {
        println(part1(input))
    }

    runMeasuredTime {
        println(part1(input, 5))
    }
}

fun List<String>.parse() = buildMap {
    forEachIndexed { y, line ->
        line.forEachIndexed { x, value ->
            put(Point(x , y), value.toString().toInt())
        }
    }
}

fun Map<Point, Int>.parseToFull(multiplication: Int): Map<Point, Int> {
    val (rows, cols) = maxOf { it.key.y } + 1 to maxOf { it.key.x } + 1
    return buildMap {
        for (x in 0 until rows * multiplication) {
            for (y in 0 until cols * multiplication) {
                val (originX, originY) = x % cols to y % rows
                val originRisk = this@parseToFull[Point(originX, originY)]!!
                val weight = x / cols + y / rows
                val newRisk = (originRisk + weight - 1) % 9 + 1
                put(Point(x, y), newRisk)
            }
        }
    }
}

fun printField(field: Map<Point, Int>) {
    val (rows, cols) = field.maxOf { it.key.y } to field.maxOf { it.key.x }
    repeat(rows + 1) { y ->
        repeat(cols + 1) { x ->
            print(field.getOrDefault(Point(x, y), 0))
        }
        print("\n")
    }
}

data class QPoint(val x: Int, val y: Int, val weight: Int) : Comparable<QPoint> {

    override fun compareTo(other: QPoint): Int = weight compareTo other.weight

    fun xy() = Point(x, y)
}
