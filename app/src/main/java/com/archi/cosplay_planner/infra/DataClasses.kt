package com.archi.cosplay_planner.infra

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateClass {
    data class FullDate(
        var day : Int,
        var month : Int,
        var year : Int
    )

}

fun fullDateToString(date: DateClass.FullDate): String {
    var month = date.month
    if (month == 12) {
        month = 1
    } else {
        month += 1
    }

    var monthResult = month.toString()

    if (monthResult.length == 1) {
        monthResult = "0" + monthResult
    }

    var dayResult = date.day.toString()

    if (dayResult.length == 1) {
        dayResult = "0" + dayResult
    }

    return dayResult + "." + monthResult + "." + date.year.toString()
}

fun stringToData(string: String): DateClass.FullDate {
    val tempArray: List<String> = string.split(".")
    return DateClass.FullDate(tempArray[0].toInt(), tempArray[1].toInt(), tempArray[2].toInt())
}

fun sortValueFromDate(string: String): Int {
    var value = 0
    val tempArray: List<String> = string.split(".")
    val date = DateClass.FullDate(tempArray[0].toInt(), tempArray[1].toInt(), tempArray[2].toInt())
    value = date.year*365 + date.month*31 + date.day
    return value
}

@RequiresApi(Build.VERSION_CODES.O)
fun checkIfInFuture(string: String): Int {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
    val currentString = LocalDateTime.now().format(formatter).toString()
    val tempArray: List<String> = string.split(".")
    val fullDate = DateClass.FullDate(tempArray[0].toInt(), tempArray[1].toInt(), tempArray[2].toInt())
    val splitArray: List<String> = currentString.split(".")
    val currentObj = DateClass.FullDate(splitArray[0].toInt(), splitArray[1].toInt(), splitArray[2].toInt())

    if (currentObj.year<fullDate.year)
    {
        return 2
    }
    if (currentObj.year==fullDate.year && currentObj.month<fullDate.month)
    {
        return 2
    }
    if (currentObj.year==fullDate.year && currentObj.month==fullDate.month && currentObj.day<fullDate.day)
    {
        return 2
    }

    if (currentObj.year==fullDate.year && currentObj.month==fullDate.month && currentObj.day==fullDate.day)
    {
        return 1
    }

    return 0
}