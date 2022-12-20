package y2022

import general.Day

object Day20 : Day() {
    override val name = "Grove Positioning System"

    class Node(val num: Long, val inc: Int) {
        var prev = this
        var next = this
    }

    private fun createNodes(nums: List<Long>): Array<Node> {
        val N = nums.size - 1
        val nodes = Array(nums.size) {

            var inc = nums[it]
            inc %= N
            // next 3 lines are "optional"
            inc += N
            inc %= N
            if (inc > N * 2) inc -= N

            Node(nums[it], inc.toInt())
        }

        for (i in nodes.indices) {
            nodes[i].prev = nodes[(i - 1 + nums.size) % nums.size]
            nodes[i].next = nodes[(i + 1) % nums.size]
        }

        return nodes
    }

    private fun shuffle(nodes: Array<Node>) {
        for (n in nodes) {
            if (n.inc == 0) continue

            // remove
            n.prev.next = n.next
            n.next.prev = n.prev

            // find
            var node = n
            if (n.inc > 0) repeat( n.inc) { node = node.next }
            if (n.inc < 0) repeat(-n.inc) { node = node.prev }
            n.prev = node
            n.next = node.next

            // add
            n.prev.next = n
            n.next.prev = n
        }
    }

    private fun sum(nodes: Array<Node>): Long {
        var sum = 0L
        val root = nodes.first { it.num == 0L }

        var node = root
        repeat(1000) { node = node.next } ; sum += node.num
        repeat(1000) { node = node.next } ; sum += node.num
        repeat(1000) { node = node.next } ; sum += node.num

        return sum
    }

    override fun a1() {
        val nums = INPUT.readLines().map { it.toLong() }
        val nodes = createNodes(nums)

        shuffle(nodes)

        println(sum(nodes))
    }

    override fun a2() {
        val nums = INPUT.readLines().map { it.toLong() * 811589153L }
        val nodes = createNodes(nums)

        repeat(10) {
            shuffle(nodes)
        }

        println(sum(nodes))
    }
}