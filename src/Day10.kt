import utils.Stack
import utils.middleItem

fun main() {
    fun part1(input: List<String>, processTail: (tail: Stack<Bracket>) -> Unit = {}): Int {
        var sum = 0
        input.forEach line@{ line ->
            val bracketStack = Stack<Bracket>()
            line.forEach { bracket ->
                when (bracket) {
                    in openBrackets -> bracketStack.push(defineBracket(bracket))
                    in closeBrackets -> if (isBracketValid(bracket, bracketStack.pop()).not()) {
                        sum += defineBracket(bracket).score
                        return@line
                    }
                    else -> Unit
                }
            }
            processTail(bracketStack)
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        val tailsScores = mutableListOf<Long>()
        part1(input) { tail ->
            var score = 0L
            while(tail.size() > 0) {
                score = score * 5 + tail.pop()!!.scoreToClose
            }
            tailsScores += score
        }
        return tailsScores.sorted().middleItem() ?: throw RuntimeException("Not odd count in list")
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    runMeasuredTime {
        println(part1(input))
    }
    runMeasuredTime {
        println(part2(input))
    }
}

enum class Bracket(val score: Int, val scoreToClose: Int) {
    ROUND(3, 1),
    SQUARE(57, 2),
    CURLY(1197, 3),
    ANGLE(25137, 4)
}

val openBrackets = listOf('(', '[', '{', '<')
val closeBrackets = listOf(')', ']', '}', '>')

fun defineBracket(symbol: Char) = when (symbol) {
    '(', ')' -> Bracket.ROUND
    '[', ']' -> Bracket.SQUARE
    '{', '}' -> Bracket.CURLY
    '<', '>' -> Bracket.ANGLE
    else -> throw RuntimeException("Unexpected bracket")
}

fun isBracketValid(symbol: Char, bracket: Bracket?) = when {
    bracket == null -> false
    symbol == ')' -> bracket == Bracket.ROUND
    symbol == ']' -> bracket == Bracket.SQUARE
    symbol == '}' -> bracket == Bracket.CURLY
    symbol == '>' -> bracket == Bracket.ANGLE
    else -> throw RuntimeException("Unexpected close bracket")
}