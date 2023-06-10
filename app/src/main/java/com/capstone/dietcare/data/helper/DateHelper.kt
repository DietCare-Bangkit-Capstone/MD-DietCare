package com.capstone.dietcare.data.helper

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun String.withDateFormat(): String {
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val date = format.parse(this) as Date
        return DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US).format(date)
    }

    fun String.withNewsDateFormat(): String {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.US)
        val date = format.parse(this) as Date
        return DateFormat.getDateInstance(DateFormat.FULL, Locale.US).format(date)
    }
}