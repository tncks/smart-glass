package com.smart.app.util

import android.os.Build
import java.time.LocalDate

class DateIncrementer {
    fun addOneDay(date: String): String {
        return if (Build.VERSION.SDK_INT >= 26) {
            LocalDate.parse(date).plusDays(1).toString()
        } else {
            org.joda.time.LocalDate.parse(date).plusDays(1).toString()
        }
    }
}