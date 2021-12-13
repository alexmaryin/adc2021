package utils

data class Line(val start: Point, val end: Point, val direction: LineDirection)

enum class LineDirection { HORIZONTAL, VERTICAL, LEFT_DIAGONAL, RIGHT_DIAGONAL }