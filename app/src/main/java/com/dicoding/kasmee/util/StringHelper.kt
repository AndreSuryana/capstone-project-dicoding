package com.dicoding.kasmee.util

object StringHelper {

    fun formatIntoIDR(number: Int): String {

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
}