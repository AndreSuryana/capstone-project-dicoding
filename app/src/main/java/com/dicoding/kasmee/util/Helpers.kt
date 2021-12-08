package com.dicoding.kasmee.util

import com.bumptech.glide.Glide
import com.dicoding.kasmee.R
import com.dicoding.kasmee.util.Constants.INPUT_PATTERN_DATE
import com.dicoding.kasmee.util.Constants.OUTPUT_PATTERN_DATE
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.*

fun dateFormat(date: String?): String? {
    val inputPattern = INPUT_PATTERN_DATE
    val outputPattern = OUTPUT_PATTERN_DATE
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