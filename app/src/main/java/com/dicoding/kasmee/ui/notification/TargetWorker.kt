package com.dicoding.kasmee.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.preference.PreferenceManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.model.response.cash.Cash
import com.dicoding.kasmee.ui.detail.cash.DetailCashActivity
import com.dicoding.kasmee.util.Constants.EXTRA_CASH
import com.dicoding.kasmee.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.dicoding.kasmee.util.Constants.NOTIFICATION_TARGET_CHANNEL_ID
import com.dicoding.kasmee.util.Constants.NOTIFICATION_TARGET_ID
import com.dicoding.kasmee.util.SerializerHelper.deserializeFromJson

class TargetWorker(
    context: Context,
    params: WorkerParameters,
) : Worker(context, params) {

    private val data = inputData.getString(EXTRA_CASH)

    override fun doWork(): Result {
        return try {
            val prefManager = PreferenceManager.getDefaultSharedPreferences(applicationContext)
            val shouldNotify = prefManager.getBoolean(
                applicationContext.getString(R.string.pref_key_notify_target_reminder),
                false
            )

            val cash = deserializeFromJson(data)

            if (shouldNotify) {
                cash?.let {
                    if (it.totalProfit >= it.target) {
                        showTargetReminderNotification(it)
                    }
                }
            }

            Result.success()
        } catch (t: Throwable) {
            Result.failure()
        }
    }

    private fun showTargetReminderNotification(content: Cash) {
        val intent = Intent(applicationContext, DetailCashActivity::class.java).apply {
            putExtra(DetailCashActivity.EXTRA_CASH_ID, content.id)
        }

        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_TARGET_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(
                    applicationContext.getString(
                        R.string.notify_target_reminder_title,
                        content.name
                    )
                )
                .setContentText(applicationContext.getString(R.string.notify_target_reminder_content))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_TARGET_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notification.setChannelId(NOTIFICATION_TARGET_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_TARGET_ID, notification.build())
    }
}