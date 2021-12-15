package com.dicoding.kasmee.util

import com.bumptech.glide.Glide
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.util.Constants.DATE_PATTERN
import com.dicoding.kasmee.util.Constants.OUTPUT_DATE_PATTERN
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

fun dateFormat(date: String?): String? {
    val inputPattern = DATE_PATTERN
    val outputPattern = OUTPUT_DATE_PATTERN
    val locale = Locale("in", "ID")

    val inputFormat = SimpleDateFormat(inputPattern, locale)
    val outputFormat = SimpleDateFormat(outputPattern, locale)

    val inputDate = date?.let { inputFormat.parse(it) }

    return inputDate?.let { outputFormat.format(it) }
}

fun CircleImageView.loadImage(image: String?) {
    Glide.with(this.context)
        .load(image)
        .placeholder(R.drawable.placeholder)
        .error(R.drawable.placeholder)
        .into(this)
}

// Serialize a single object.
fun serializeToJson(cash: Cash?): String? {
    val gson = Gson()
    return gson.toJson(cash)
}

// Deserialize to single object.
fun deserializeFromJson(jsonString: String?): Cash? {
    val gson = Gson()
    return gson.fromJson(jsonString, Cash::class.java)
}