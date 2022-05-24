package com.smart.app.util

import android.graphics.Color
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

@Suppress("unused")
fun getDatesBetween(dateString1: String, dateString2: String): List<String> {
    val dates = ArrayList<String>()
    val input = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
    var date1: Date? = null
    var date2: Date? = null
    try {
        date1 = input.parse(dateString1)
        date2 = input.parse(dateString2)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val cal1 = Calendar.getInstance()
    cal1.time = date1!!
    val cal2 = Calendar.getInstance()
    cal2.time = date2!!
    while (!cal1.after(cal2)) {
        val output = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
        dates.add(output.format(cal1.time))
        cal1.add(Calendar.DATE, 1)
    }
    return dates
}


fun addChar(str: String, ch: Char, position: Int): String {
    return (str.substring(0, position) + ch + str.substring(position))
}


fun getIntegerPrimaryColorCode(): Int {
    return Color.parseColor("#6970FD")
}

fun getIntegerBlackColorCode(): Int {
    return Color.parseColor("#000000")
}


// Refer
// End of util


//fun customNextDayFormatting(dateParam: Date?): String {
//    val plusedDateParam: Date? = calcTommorowDateTimeExactly(dateParam)
//    val cad = Calendar.getInstance()
//    cad.time = plusedDateParam!!
//    val output = SimpleDateFormat("yyyy-MM-dd", Locale.KOREA)
//    return output.format(cad.time)
//}
//
//
//fun calcTommorowDateTimeExactly(param: Date?): Date? {
//    var next: Date? = null
//    param?.let {
//        next = Date(it.time.plus(MILLIS_IN_A_DAY))
//    }
//    return next
//}