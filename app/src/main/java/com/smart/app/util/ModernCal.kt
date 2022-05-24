package com.smart.app.util

import android.os.Build
import android.util.Log
import java.text.ParseException

fun getDatesBetweenModernNew(dateString1: String, dateString2: String): List<String> {

    if (Build.VERSION.SDK_INT >= 26) {

        val dates = ArrayList<String>()
        var flag: Boolean? = null
        val input = java.time.format.DateTimeFormatter.ofPattern("[uu-MM-dd][uuuu-MM-dd]")

        var date1: java.time.LocalDate
        var date2: java.time.LocalDate
        try {
            date1 = java.time.LocalDate.parse(dateString1)
            date2 = java.time.LocalDate.parse(dateString2)

            flag = true
        } catch (e: ParseException) {

            flag = false

            date1 = java.time.LocalDate.parse("2023-01-01", input)
            date2 = java.time.LocalDate.parse("2023-01-05", input)
        } catch (e: Exception) {

            flag = false

            date1 = java.time.LocalDate.parse("2023-01-01")
            date2 = java.time.LocalDate.parse("2023-01-05")
        }

        if (flag == true) {
            while (!date1.isAfter(date2)) {
                val output = java.time.format.DateTimeFormatter.ofPattern("uuuu-MM-dd")
                dates.add(date1.format(output))
                date1 = date1.plusDays(1)
            }
        }


        return dates


    } else {

        val dates = ArrayList<String>()
        var flag: Boolean? = null
        // val input = org.joda.time.format.DateTimeFormat.forPattern("yyyy-MM-dd")

        var date1: org.joda.time.LocalDate
        var date2: org.joda.time.LocalDate
        try {
            date1 = org.joda.time.LocalDate.parse(dateString1)
            date2 = org.joda.time.LocalDate.parse(dateString2)

            flag = true
        } catch (e: Exception) {

            flag = false

            date1 = org.joda.time.LocalDate.parse("2023-01-01")
            date2 = org.joda.time.LocalDate.parse("2023-01-05")
        }

        if (flag == true) {
            while (!date1.isAfter(date2)) {
                val output = "yyyy-MM-dd"
                dates.add(date1.toString(output))
                date1 = date1.plusDays(1)
            }
        }


        return dates


    }


}


// Short Month and Day -> Long Month and Day
// String Extension Util For Date Formatting
/*---------------------------------------*/

fun convertingForm(s: String): String {
    val array = s.split("-")

    if (array.size != 3) {
        Log.d("arraysizeErr", "arraySizeError")
        return s
    }


    val shortMonth = array[1]
    var longMonth = "0"

    if (shortMonth.length == 1) {
        longMonth += shortMonth
    } else {
        longMonth = shortMonth
    }

    val shortDay = array[2]
    var longDay = "0"

    if (shortDay.length == 1) {
        longDay += shortDay
    } else {
        longDay = shortDay
    }


    return ("20" + array[0] + "-" + longMonth + "-" + longDay)
}


/*---------------------------------------*/
/*---------------------------------------*/
// 혹시 모를 오류에 대비한 백업 코드
//            date1 = java.time.LocalDate.parse(dateString1, input)
//            date2 = java.time.LocalDate.parse(dateString2, input)
/*
date1 = org.joda.time.LocalDate.parse(dateString1, input)
date2 = org.joda.time.LocalDate.parse(dateString2, input)
 */