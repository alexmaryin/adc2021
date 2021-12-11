fun main() {
    fun part1(input: List<InputLine>): Int {
        val result = input.sumOf { line ->
            line.output.count { it.length in listOf(2, 3, 4, 7) }
        }
        return result
    }

    fun part2(input: List<InputLine>): Int {
        return input.sumOf {
            val digits = decryptDigits(it.digits)
            val result = decryptNumber(digits, it.output)
            println("${it.digits}: $result")
            result
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08_test")
    check(part1(parseInput8(testInput)) == 26)
    check(part2(parseInput8(testInput)) == 61229)

    val input = readInput("day08")
    println(part1(parseInput8(input)))
    println(part2(parseInput8(input)))
}

fun parseInput8(input: List<String>) = input.map {
    val (left, right) = it.split(" | ", limit = 2)
    InputLine(
        digits = left.split(" ").map { seq -> seq.toList().sorted().joinToString("") },
        output = right.split(" ").map { seq -> seq.toList().sorted().joinToString("") }
    )
}

fun decryptDigits(input: List<String>): Map<String, Int> {
    val digits = emptyMap<Int, String>().toMutableMap()
    digits[1] = input.find { it.length == 2 }!!
    digits[7] = input.find { it.length == 3 }!!
    digits[4] = input.find { it.length == 4 }!!
    digits[8] = input.find { it.length == 7 }!!
    digits[9] = input.find { it.length == 6 && it.containsAllOf(digits[1]!!) && it.containsAllOf(digits[4]!!)}!!
    digits[6] = input.find { it.length == 6 && digits[8]!!.containsAllOf(it) && it.containsAllOf(digits[1]!!).not() }!!
    digits[0] = input.find { it.length == 6 && it != digits[6]!! && it != digits[9]!! }!!
    digits[3] = input.find { it.length ==5 && it.containsAllOf(digits[1]!!) }!!
    digits[5] = input.find { it.length ==5 && it != digits[3]!! && digits[9]!!.containsAllOf(it) }!!
    digits[2] = input.find { it.length ==5 && it != digits[3]!! && it != digits[5]!! }!!
    return digits.map { it.value to it.key }.toMap()
}

fun String.containsAllOf(other: String): Boolean = other.map { contains(it) }.all { it }

fun decryptNumber(digits: Map<String, Int>, encrypted: List<String>): Int = buildString {
    encrypted.forEach { append(digits[it]) }
}.toInt()

data class InputLine(val digits: List<String>, val output: List<String>)