package com.archi.cosplay_planner.P_Infra

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class Dataclasses {

    data class fulldata(
        var day : Int,
        var month : Int,
        var year : Int
    )


}

fun fulldata_to_string(date: Dataclasses.fulldata): String {
    var month = date.month

    if (month==12)
    {
        month=1
    }
    else
    {
        month += 1
    }

    var month_s = month.toString()

    if (month_s.length==1)
    {
        month_s="0"+month_s
    }

    var day_s = date.day.toString()

    if (day_s.length==1)
    {
        day_s="0"+day_s
    }



    val result = day_s+"."+month_s+"."+date.year.toString()
    return result
}

fun string_to_data(string: String): Dataclasses.fulldata {
    val arr: List<String> = string.split(".")
    val date = Dataclasses.fulldata(arr[0].toInt(), arr[1].toInt(), arr[2].toInt())
    return date
}

fun sort_value_from_date(string: String): Int {
    var value = 0
    val arr: List<String> = string.split(".")
    val date = Dataclasses.fulldata(arr[0].toInt(), arr[1].toInt(), arr[2].toInt())
    value = date.year*365 + date.month*31 + date.day
    return value
}

@RequiresApi(Build.VERSION_CODES.O)
fun check_if_in_future(string: String): Int {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val current_string = LocalDateTime.now().format(formatter).toString()
    val arr_s: List<String> = string.split(".")
    val date_obj = Dataclasses.fulldata(arr_s[0].toInt(), arr_s[1].toInt(), arr_s[2].toInt())
    val arr_c: List<String> = current_string.split(".")
    var current_obj = Dataclasses.fulldata(arr_c[0].toInt(), arr_c[1].toInt(), arr_c[2].toInt())

    if (current_obj.year<date_obj.year)
    {
        return 2
    }
    if (current_obj.year==date_obj.year && current_obj.month<date_obj.month)
    {
        return 2
    }
    if (current_obj.year==date_obj.year && current_obj.month==date_obj.month && current_obj.day<date_obj.day)
    {
        return 2
    }

    if (current_obj.year==date_obj.year && current_obj.month==date_obj.month && current_obj.day==date_obj.day)
    {
        return 1
    }

    return 0
}