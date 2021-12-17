import kotlin.math.max

fun main() {
    fun part1(input: List<String>) {
        val (areaX, areaY) = input.parseInitials()
        val (minX, maxX) = areaX
        val (maxY, minY) = areaY

        var maxHeight = minY
        var count = 0

        for (velXt in 1..maxX * 2) {
            for (velYt in -200..200) {
                var (x, y, height) = listOf(0, 0, minY)
                var (velX, velY) = listOf(velXt, velYt)
                while (x <= maxX && y >= maxY) {
                    x += velX
                    if (velX != 0) velX -= if (velX > 0) 1 else -1

                    y += velY
                    velY--

                    height = max(height, y)

                    if (x in minX..maxX && y in maxY..minY) {
//                        println("With vX=$velXt vy=$velYt height was $height")
                        maxHeight = max(maxHeight, height)
                        count++
                        break
                    }
                }
            }
        }
        println(maxHeight)
        println(count)
    }


    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day17_test")
    part1(testInput)

    val input = readInput("day17")
    part1(input)
}

fun List<String>.parseInitials(): Pair<List<Int>, List<Int>> {
    val (areaX, areaY) = first().split(",", limit = 2)
    val areaXList = areaX.substringAfter("x=").split("..").map { it.toInt() }
    val areaYList = areaY.substringAfter("y=").split("..").map { it.toInt() }
    return areaXList to areaYList
}

