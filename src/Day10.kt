import utils.Stack

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        input.forEach line@{ line ->
            val bracketStack = Stack<Bracket>()
            line.forEach { bracket ->
                when (bracket) {
                    in openBrackets -> bracketStack.push(defineBracket(bracket))
                    in closeBrackets -> if (isBracketValid(bracket, bracketStack.peek())) {
                        bracketStack.pop()
                    } else {
                        sum += defineBracket(bracket).score
                        return@line
                    }
                    else -> Unit
                }
            }
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)

    val input = readInput("Day10")
    runMeasuredTime {
        println(part1(input))
    }
}

enum class Bracket(val score: Int) {
    ROUND(3),
    SQUARE(57),
    CURLY(1197),
    ANGLE(25137)
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