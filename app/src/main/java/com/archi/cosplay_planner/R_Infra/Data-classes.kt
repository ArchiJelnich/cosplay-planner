package com.archi.cosplay_planner.R_Infra

class Dataclasses {

    data class fulldata(
        var day : Int,
        var month : Int,
        var year : Int
    )


}

fun fulldata_to_string(day: Int, month: Int, year: Int): String {
    val result = day.toString()+"."+month.toString()+"."+year.toString()
    return result
}

fun string_to_data(string: String): Dataclasses.fulldata {
    val arr: List<String> = string.split(".")
    val date = Dataclasses.fulldata(arr[0].toInt(), arr[1].toInt(), arr[2].toInt())
    return date
}