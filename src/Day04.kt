fun main() {
    fun part1(balls: List<Int>, boards: List<Board>): Int {
        balls.forEach { ball ->
            boards.forEach { board ->
                board.checkBall(ball)
                if (board.win) {
                    println("Board win: $board with ball $ball")
                    return board.sum * ball
                }
            }
        }
        return -1
    }

    fun part2(balls: List<Int>, boards: List<Board>): Int {
        var lastResult = -1
        balls.forEach { ball ->
            boards.filterNot { it.win }.forEach boards@{ board ->
                board.checkBall(ball)
                if (board.win) {
                    println("Board win: $board with ball $ball")
                    lastResult = board.sum * ball
                }
            }
        }
        return lastResult
    }

    // test if implementation meets criteria from the description, like:
    runMeasuredTime {
        val testInput = readInput("Day04_test")
        val testBalls = testInput[0].split(",").map { it.toInt() }
        val testBoards = inputParse(testInput.drop(2).filterNot { it.isBlank() })
        // Due to mutable state of boards you shall to run tests separately
//        check(part1(testBalls, testBoards) == 4512)
        check(part2(testBalls, testBoards) == 1924)
    }

    runMeasuredTime {
        val input = readInput("Day04")
        val balls = input[0].split(",").map { it.toInt() }
        val boards = inputParse(input.drop(2).filterNot { it.isBlank() })
//        println(part1(balls, boards))
        println(part2(balls, boards))
    }

//    println(part2(input))
}

data class Board(
    val numbers: List<List<Int>>,
    val maxInRows: MutableList<Int>,
    val maxInCols: MutableList<Int>,
    var sum: Int,
    var win: Boolean = false
) {
    fun checkBall(ball: Int) {
        numbers.forEachIndexed { row, line ->
            val col = line.indexOf(ball)
            if (col != -1) {
                maxInRows[row] += 1
                maxInCols[col] += 1
                sum -= ball
                if (maxInRows[row] == 5 || maxInCols[col] == 5) win = true
            }
        }
    }
}

fun inputParse(raw: List<String>, size: Int = 5): List<Board> {
    var cursor = 0
    return buildList {
        while (cursor < raw.size - 2) {
            var sum = 0
            add(
                Board(
                    numbers = buildList {
                        do {
                            add(raw[cursor].split(" ").filterNot { it.isBlank() }.map { it.toInt() }
                                .also { sum += it.sum() })
                            cursor++
                        } while (cursor % size != 0)
                    },
                    maxInRows = MutableList(size) { 0 },
                    maxInCols = MutableList(size) { 0 },
                    sum = sum
                )
            )
        }
    }
}