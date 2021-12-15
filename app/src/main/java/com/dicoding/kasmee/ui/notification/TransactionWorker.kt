package com.dicoding.kasmee.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.ui.main.MainActivity
import com.dicoding.kasmee.util.Constants
import com.dicoding.kasmee.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.dicoding.kasmee.util.Constants.NOTIFICATION_DAILY_REMINDER_CHANNEL_ID
import com.dicoding.kasmee.util.Constants.NOTIFICATION_DAILY_REMINDER_ID
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.*

@HiltWorker
class TransactionWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val repository: KasmeeRepository
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat(Constants.DATE_PATTERN, Locale.getDefault())
            val today = dateFormat.format(currentTime)

            val result = repository.getTodayTransaction(today)
            val transaction = result.data

            if (transaction?.createdAt.isNullOrEmpty()) {
                showTransactionReminderNotification()
            }

            Result.success()
        } catch (t: Throwable) {
            Result.failure()
        }
    }

    private fun showTransactionReminderNotification() {
        val intent = Intent(applicationContext, MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(applicationContext).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat.Builder(applicationContext, NOTIFICATION_DAILY_REMINDER_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(applicationContext.getString(R.string.notify_daily_reminder_title))
                .setContentText(applicationContext.getString(R.string.notify_daily_reminder_content))
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_DAILY_REMINDER_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notification.setChannelId(NOTIFICATION_DAILY_REMINDER_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_DAILY_REMINDER_ID, notification.build())
    }
}