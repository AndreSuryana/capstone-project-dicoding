package com.dicoding.kasmee.util

import java.text.SimpleDateFormat
import java.util.*

object StringHelper {

    fun formatIntoIDR(number: Long): String {

        val numberStr = number.toString()
        val stringBuilder = StringBuilder(numberStr)
        var idx = numberStr.length // Starting index (from the back of the string)
        val hop = 3 // The number of skiped index

        while (idx > hop) {
            idx -= 3
            stringBuilder.insert(idx, '.')
        }

        return stringBuilder.toString()
    }

    fun getTargetPercentage(target: Long, totalProfit: Long): String {
        val result = (totalProfit.toDouble() / target) * 100
        var stringResult = String.format("%2.02f", result)

        if (result >= 100)
            stringResult = "100"

        return stringResult
    }

    fun dateFormat(date: String?): String? {
        val inputPattern = Constants.DATE_PATTERN
        val outputPattern = Constants.OUTPUT_DATE_PATTERN
        val locale = Locale("in", "ID")

        val inputFormat = SimpleDateFormat(inputPattern, locale)
        val outputFormat = SimpleDateFormat(outputPattern, locale)

        val inputDate = date?.let { inputFormat.parse(it) }

        return inputDate?.let { outputFormat.format(it) }
    }
}