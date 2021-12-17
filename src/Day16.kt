import java.math.BigInteger

fun main() {
    fun part1(input: String, abc: Map<String, String>): Int {
        val decoded = input.map { abc[it.toString()] }.joinToString("")
        val (item, _) = processNext(decoded)
        return item.countVersion()
    }

    fun part2(input: String, abc: Map<String, String>): BigInteger {
        val decoded = input.map { abc[it.toString()] }.joinToString("")
        val (item, _) = processNext(decoded)
        return item.expression()
    }

    val abc = readInput("day16abc").associate { line ->
        val (symbol, code) = line.split(" = ", limit = 2)
        symbol to code
    }

    // test if implementation meets criteria from the description, like:
    check(part1("D2FE28", abc) == 6)
    check(part1("38006F45291200", abc) == 9)
    check(part1("EE00D40C823060", abc) == 14)
    check(part1("8A004A801A8002F478", abc) == 16)
    check(part1("620080001611562C8802118E34", abc) == 12)
    check(part1("C0015000016115A2E0802F182340", abc) == 23)
    check(part1("A0016C880162017C3686B18A3D4780", abc) == 31)

    val input = readInput("day16")
    runMeasuredTime {
        println(part1(input.first(), abc))
    }
    runMeasuredTime {
        println(part2(input.first(), abc))
    }
}

sealed class Item(var version: Int = 0) {
    abstract fun countVersion(): Int
    abstract fun expression(): BigInteger

    data class Literal(val value: BigInteger) : Item() {
        override fun countVersion() = version
        override fun expression(): BigInteger = value
    }

    data class Operator(val literals: List<Item>, var operator: Int = 0) : Item() {
        override fun countVersion() = version + literals.sumOf { it.countVersion() }

        override fun expression(): BigInteger = when (operator) {
                0 -> literals.drop(1).fold(literals.first().expression()) { acc, next -> acc + next.expression() }
                1 -> literals.drop(1).fold(literals.first().expression()) { acc, next -> acc * next.expression() }
                2 -> literals.minOf { it.expression() }
                3 -> literals.maxOf { it.expression() }
                5 -> if (literals.first().expression() > literals.last().expression()) BigInteger.ONE else BigInteger.ZERO
                6 -> if (literals.first().expression() < literals.last().expression()) BigInteger.ONE else BigInteger.ZERO
                7 -> if (literals.first().expression() == literals.last().expression()) BigInteger.ONE else BigInteger.ZERO
                else -> throw RuntimeException("Illegal type of operation $operator")
            }
    }
}

fun parseHeader(header: String): Pair<Int, Int> {
    val version = header.take(3).toInt(2)
    val id = header.drop(3).take(3).toInt(2)
    return version to id
}

fun parseLiteral(next: String): Pair<Item.Literal, String> {
    var notLast = true
    var literals = ""
    var tail = next
    while (notLast) {
        val (last, literal) = with(tail.take(5)) {
            (get(0) == '0') to drop(1)
        }
        if (last) notLast = false
        literals += literal
        tail = tail.drop(5)
    }
    return Item.Literal(BigInteger(literals, 2)) to tail
}

fun parseOperatorIncluded(next: String): Pair<Item.Operator, String> {
    val tail = next.drop(1)
    val length = tail.take(15).toInt(2)
    var literalsStr = tail.drop(15).take(length)
    val finishTail = tail.drop(15).drop(length)
    val literals = mutableListOf<Item>()
    while (literalsStr.isNotBlank()) {
        val (nextLiteral, literalsStrTail) = processNext(literalsStr)
        literals += nextLiteral
        literalsStr = literalsStrTail
    }
    return Item.Operator(literals) to finishTail
}

fun parseOperatorExcluded(next: String): Pair<Item.Operator, String> {
    var tail = next.drop(1)
    val count = tail.take(11).toInt(2)
    val literals = mutableListOf<Item>()
    tail = tail.drop(11)
    repeat(count) {
        val (nextLiteral, literalStrTail) = processNext(tail)
        literals += nextLiteral
        tail = literalStrTail
    }
    return Item.Operator(literals) to tail
}

fun processNext(tail: String): Pair<Item, String> {
    val (version, id) = parseHeader(tail.take(6))
    val tailWithoutHeader = tail.drop(6)
    val (item, tailAfter) = if (id == 4) {
        parseLiteral(tailWithoutHeader)
    } else {
        val length = tailWithoutHeader[0]
        if (length == '0') parseOperatorIncluded(tailWithoutHeader)
        else parseOperatorExcluded(tailWithoutHeader)
    }
    item.version = version
    if (item is Item.Operator) item.operator = id
    return item to tailAfter
}