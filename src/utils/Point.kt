package utils

data class Point(val x: Int, val y: Int) : Comparable<Point> {
    override fun compareTo(other: Point): Int = when {
        x != other.x -> x compareTo other.x
        y != other.y -> y compareTo other.y
        else -> 0
    }
}