package y2023

import general.Day

object Day12 : Day() {
    override val name = "Hot Springs"

    override fun a1() {
        var sum = 0

        for (line in INPUT.readLines()) {
            val record = line.split(" ")[0]
            val nums = line.split(" ")[1].split(",").map { it.toInt() }

            // i: record index
            // l: length of current broken spring (-1 = no spring)
            // ni: nums index
            fun dfs(i: Int, l: Int, ni: Int): Int {
                if (i >= record.length) {
                    if (l <= 0 && ni == nums.size) return 1 // all broken springs 'placed'
                    return 0 // overshot
                }
                if (l == 0) { // spring needs separation of '.'
                    if (record[i] == '#') return 0
                    return dfs(i+1, -1, ni)
                }
                if (l > 0) { // continue 'placing' broken spring
                    if (record[i] == '.') return 0
                    return dfs(i+1, l-1, ni)
                }
                if (record[i] == '#') { // in a need of a broken spring
                    if (ni == nums.size) return 0 // oh no, no more broken springs available
                    return dfs(i+1, nums[ni]-1, ni+1) // nice, next
                }
                if (record[i] == '.') { // 'skip'
                    return dfs(i+1, -1, ni)
                }
                if (record[i] == '?') { // try both
                    val a = if (ni == nums.size) 0 else dfs(i+1, nums[ni]-1, ni+1)
                    val b = dfs(i+1, -1, ni)
                    return a + b
                }
                // ???, what input did YOU give me?! :)
                return 0
            }

            sum += dfs(0, -1, 0)
        }

        println(sum)
    }

    override fun a2() {
        var sum = 0L

        for (line in INPUT.readLines()) {
            val r = line.split(" ")[0]
            val n = line.split(" ")[1].split(",").map { it.toInt() }
            val record = "$r?$r?$r?$r?$r"
            val nums = listOf(n, n, n, n, n).flatten()

            val cache =
                Array(record.length) {
                Array(nums.size + 1) {
                    -1L
            }}
            // i: record index
            // ni: nums index
            fun dp(i: Int, ni: Int): Long {
                // base cases
                if (i >= record.length) {
                    if (ni == nums.size) return 1 // all broken springs 'placed'
                    return 0 // overshot
                }
                if (cache[i][ni] != -1L) return cache[i][ni]
                // 'place' broken spring
                val a by lazy {
                    if (ni == nums.size) return@lazy 0 // no more broken springs available
                    if (i + nums[ni] > record.length) return@lazy 0 // spring longer than available record
                    for (j in i until i + nums[ni])
                        if (record[j] == '.')
                            return@lazy 0 // spring has to be functional inside slice
                    if (i + nums[ni] < record.length) {
                        if (record[i + nums[ni]] == '#') return@lazy 0 // record demands longer broken spring
                        return@lazy dp(i + nums[ni] + 1, ni + 1)
                    }
                    return@lazy dp(i + nums[ni], ni + 1) // broken spring reaches till end of record
                }
                // 'skip'
                val b by lazy { dp(i + 1, ni) }
                // check
                if (record[i] == '#') { cache[i][ni] = a; return a }
                if (record[i] == '.') { cache[i][ni] = b; return b }
                cache[i][ni] = a + b
                return a + b
            }

            sum += dp(0, 0)
        }

        println(sum)
    }
}