fun main() {
    fun part1(input: List<String>): Int {
        val size = input[0].length
        val zeros = MutableList(size) { 0 }
        val ones = MutableList(size) { 0 }
        input.indices.forEach { line ->
            for (i in 0 until size) {
                if (input[line][i] == '0') zeros[i]++ else ones[i]++
            }
        }
        val gamma = buildString {
            for (i in 0 until size) {
                append(if (zeros[i] > ones[i]) "0" else "1")
            }
        }
        val epsilon = gamma.inverseBinary()
        return gamma.toInt(2) * epsilon.toInt(2)
    }

    fun binaryFilter(list: List<String>, mostCommon: Boolean, bit: Int = 0): String {
        if (list.size == 1)
            return list[0]

        val targetBit = list.fold(0 to 0) { (zeros, ones), line ->
            if (line[bit] == '0') zeros + 1 to ones else zeros to ones + 1
        }.let {
            when (mostCommon) {
                true -> if (it.first > it.second) '0' else '1'
                false -> if (it.first > it.second) '1' else '0'
            }
        }
        return binaryFilter(list.filter { it[bit] == targetBit }, mostCommon, bit + 1)
    }

    fun part2(input: List<String>): Int {
        val oxygenRate = binaryFilter(input, mostCommon = true)
        val co2Rate = binaryFilter(input, mostCommon = false)
        return oxygenRate.toInt(2) * co2Rate.toInt(2)
    }

//     test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    runMeasuredTime {
        println(part1(input))
    }
    runMeasuredTime {
        println(part2(input))
    }
}

fun String.inverseBinary() = map { if (it == '0') '1' else '0' }.joinToString("")