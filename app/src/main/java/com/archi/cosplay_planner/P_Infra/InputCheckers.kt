package com.archi.cosplay_planner.P_Infra

    fun InputCheckerText(string: String): Pair<String, Int> {
        var status = 0
        var new_string = string
        val regex = Regex("\n\\s*\n")
        new_string = string.replace(regex, " ")
        new_string = new_string.replace("*", "")


        if (new_string.length>20) {
            status = 1
            new_string = "Too long name!"
        }

        if (new_string.length==0) {
            status = 1
            new_string = "Wrong string!"
        }



        return Pair(new_string, status)
    }

fun IntCheckerNum(string: String): Boolean {
    val regex = "-?[0-9]+(\\.[0-9]+)?".toRegex()
    return string.matches(regex)
}