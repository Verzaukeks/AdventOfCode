package y2020

object Day19 : Day() {

    override val day = 19
    override val name = "Monster Messages"

    data class Rule(val id: Int, var content: String, var isRegex: Boolean)

    override fun a1() {
        val input = INPUT.readText().replace("\r", "").split("\n\n")

        val rulesRaw = input[0].split("\n")
        val rules = Array(rulesRaw.size) { Rule(it, "", false) }
        for (rule in rulesRaw)
            rules[
                rule.substringBefore(": ").toInt()
            ].apply {
                content = rule.substringAfter(": ")
                isRegex = content.startsWith("\"")
                if (isRegex) content = content.substring(1, content.length-1)
            }

        // magically create regex expressions
        var finished = false
        while (!finished) {
            finished = true

            r@for (rule in rules) {
                if (rule.isRegex) continue@r
                var content = ""
                var ors = 0

                for (or in rule.content.split(" | ")) {
                    if (ors++ != 0) content += "|"
                    for (and in or.split(" ").map { it.toInt() }) {
                        if (!rules[and].isRegex) {
                            finished = false
                            continue@r
                        }
                        content += rules[and].content
                    }
                }

                rule.content = if (ors == 1) content else "($content)"
                rule.isRegex = true
            }
        }

        // output
        val regex = rules[0].content.toRegex()
        val messages = input[1].split("\n")
        val amount = messages.count { regex.matchEntire(it) != null }
        println(amount)
    }

    override fun a2() {
        val input = INPUT.readText().replace("\r", "").split("\n\n")

        val rulesRaw = input[0].split("\n")
        val rules = Array(rulesRaw.size) { Rule(it, "", false) }
        for (rule in rulesRaw)
            rules[
                    rule.substringBefore(": ").toInt()
            ].apply {
                content = rule.substringAfter(": ")
                isRegex = content.startsWith("\"")
                if (isRegex) content = content.substring(1, content.length-1)
            }

        // magically create regex expressions
        var finished = false
        while (!finished) {
            finished = true

            r@for (rule in rules) {
                if (rule.isRegex) continue@r
                var content = ""
                var ors = 0

                for (or in rule.content.split(" | ")) {
                    if (ors++ != 0) content += "|"
                    for (and in or.split(" ").map { it.toInt() }) {
                        if (!rules[and].isRegex) {
                            finished = false
                            continue@r
                        }
                        content += rules[and].content
                    }
                }

                rule.content = if (ors == 1) content else "($content)"
                rule.isRegex = true

                // Hehe, there has to be something better... xD
                val r42 = rules[42].content
                val r31 = rules[31].content
                if (rule.id == 8) rule.content = "($r42)+"
                if (rule.id == 11) rule.content =
                        "($r42$r31" +
                        "|$r42$r42$r31$r31" +
                        "|$r42$r42$r42$r31$r31$r31" +
                        "|$r42$r42$r42$r42$r31$r31$r31$r31" +
                        "|$r42$r42$r42$r42$r42$r31$r31$r31$r31$r31)"
            }
        }

        // output
        val regex = rules[0].content.toRegex()
        val messages = input[1].split("\n")
        val amount = messages.count { regex.matchEntire(it) != null }
        println(amount)
    }

}