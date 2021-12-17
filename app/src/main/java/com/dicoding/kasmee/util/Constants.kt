package com.dicoding.kasmee.util

object Constants {

//    const val BASE_URL = "http://kasmee.herokuapp.com/api/"
    const val BASE_URL = "http://192.168.1.2:8000/api/"
    const val USER_TOKEN = "user_token"

    // Paging
    const val CASH_STARTING_PAGE_INDEX = 1
    const val TRANSACTION_STARTING_PAGE_INDEX = 1

    // Time Format
    const val DATE_PATTERN = "yyyy-MM-dd"
    const val OUTPUT_DATE_PATTERN = "dd MMM yyyy"

    // Notification
    const val NOTIFICATION_CHANNEL_NAME = "Kasmee"

    const val NOTIFICATION_DAILY_REMINDER_ID = 102
    const val NOTIFICATION_DAILY_REMINDER_CHANNEL_ID = "notify-daily"

    const val NOTIFICATION_TARGET_ID = 103
    const val NOTIFICATION_TARGET_CHANNEL_ID = "notify-target"

    // Work Manager
    const val EXTRA_CASH = "extra_cash"
    const val DAILY_UNIQUE_WORK = "Daily Reminder"
    const val TARGET_UNIQUE_WORK = "Target Reminder"
}