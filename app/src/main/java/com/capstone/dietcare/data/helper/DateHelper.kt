package com.capstone.dietcare.data.helper

import android.os.Build
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.util.*

object DateHelper {
    fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()
        return dateFormat.format(date)
    }

    fun getAge(birthdate: String): Int {
        val birthInstance = Calendar.getInstance()
        birthInstance.set(birthdate.removeRange(4,10).toInt(), birthdate.removeRange(0,5).removeRange(2,5).toInt(), birthdate.removeRange(0,8).toInt())
        val birthInMilli = birthInstance.timeInMillis
        val now = Date().time
        val age = now - birthInMilli
        return (age.div(31556952000)).toInt()
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