fun main() {

    // Brout-force approach
    fun part1(days: Int, input: MutableList<Int>): Int {
//        println("Initial state: $input")
        for(day in 1..days) {
            var newFishes = 0
            input.forEachIndexed { i, value ->
                if (value == 0) {
                    input[i] = 6
                    newFishes++
                } else {
                    input[i] = value - 1
                }
            }
            input.addAll(List(newFishes) { 8 })
//            println("After $day day: $input")
        }
        return input.size
    }

    // Buckets approach
    fun part2(days: Int, input: List<Int>): Long {
        // Each bucket contains count of fishes with age equals bucket-index
        val buckets = buildMap {
            (0..6).forEach { put(it, input.count { age -> age == it }.toLong()) }
        }.toMutableMap()
        buckets[7] = 0
        buckets[8] = 0

        repeat(days) {
            val current = buckets[0]!!
            // Decrease fishes ages moving their count to previous bucket
            repeat(8) { buckets[it] = buckets[it + 1]!! }
            // Newborn fishes count equals fishes with 0 age from bucket 0
            buckets[8] = current
            // Renew fishes with 0 age to new cycle
            buckets[6] = buckets[6]!! + current
        }

        return buckets.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")[0].split(",").map { it.toInt() }
    runMeasuredTime {
        check(part1(18, testInput.toMutableList()) == 26)
        check(part1(80, testInput.toMutableList()) == 5934)
    }

    runMeasuredTime {
        check(part2(18, testInput.toMutableList()) == 26L)
        check(part2(80, testInput.toMutableList()) == 5934L)
        check(part2(256, testInput.toMutableList()) == 26984457539L)
    }

    val input = readInput("Day06")[0].split(",").map { it.toInt() }

    runMeasuredTime {
        println(part2(80, input))
    }

    runMeasuredTime {
        println(part2(256, input))
    }
}