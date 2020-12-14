package y2020

object d04 {
    fun a1() {
        val input = Files[4].readText()
        var amount = 0

        val passports = input.split("\r\n\r\n")
        for (passport in passports) {

            val fields = passport.split("\r\n", " ")
            var pairs = fields.size

            for (field in fields)
                if (field.startsWith("cid:"))
                    pairs--

            if (pairs == 7)
                amount ++
        }

        println(amount)
    }

    fun a2() {
        val input = Files[4].readText()
        var amount = 0

        val passports = input.split("\r\n\r\n")
        a@for (passport in passports) {

            val fields = passport.split("\r\n", " ")
            var pairs = fields.size

            for (field in fields) {
                val v = field.substringAfter(":")

                if (field.startsWith("cid:")) pairs--
                else if (field.startsWith("byr")) {
                    if (v.length != 4) continue@a
                    if (v.toInt() < 1920 || v.toInt() > 2002) continue@a
                }
                else if (field.startsWith("iyr")) {
                    if (v.length != 4) continue@a
                    if (v.toInt() < 2010 || v.toInt() > 2020) continue@a
                }
                else if (field.startsWith("eyr")) {
                    if (v.length != 4) continue@a
                    if (v.toInt() < 2020 || v.toInt() > 2030) continue@a
                }
                else if (field.startsWith("hgt")) {
                    if (field.endsWith("cm")) {
                        val n = v.substring(0, v.length - 2).toInt()
                        if (n < 150 || n > 193) continue@a
                    }
                    else  if (field.endsWith("in")) {
                        val n = v.substring(0, v.length - 2).toInt()
                        if (n < 59 || n > 76) continue@a
                    }
                    else continue@a
                }
                else if (field.startsWith("hcl")) {
                    if (!v.startsWith("#")) continue@a
                    if (v.length != 7) continue@a
                    if (v.substring(1)
                         .replace("[0-9a-f]".toRegex(), "")
                         .isNotEmpty()) continue@a
                }
                else if (field.startsWith("ecl")) {
                    if (v !in arrayOf(
                            "amb",
                            "blu",
                            "brn",
                            "gry",
                            "grn",
                            "hzl",
                            "oth",
                       )) continue@a
                }
                else if (field.startsWith("pid")) {
                    if (v.length != 9) continue@a
                }
                else continue@a
            }

            if (pairs == 7)
                amount ++
        }

        println(amount)
    }
}