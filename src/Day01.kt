fun main() {
    fun part1(input: List<Int>): Int = input.windowed(2).count { (first, second) ->
        first < second
    }

// My first approach
//        input.fold(input[0] to 0) { counter, next ->
//        if (next > counter.first) {
//            next to counter.second + 1
//        } else {
//            next to counter.second
//        }
//    }.second

    fun part2(input: List<Int>): Int = input.windowed(4).count { (first, _, _, last) ->
        first < last
    }

// My first approach
//    {
//        val windowSums = input.windowed(size = 3) { it.sum() }
//        return part1(windowSums)
//    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01_test").map { it.toInt() }
    check(part1(testInput) == 7)

    val input = readInput("day01").map { it.toInt() }
    runMeasuredTime {
        println(part1(input))
    }

    runMeasuredTime {
        println(part2(input))
    }
}
