package com.dicoding.kasmee.ui.notification

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import com.dicoding.kasmee.R
import com.dicoding.kasmee.data.repository.KasmeeRepository
import com.dicoding.kasmee.ui.main.MainActivity
import com.dicoding.kasmee.util.Constants.DATE_PATTERN
import com.dicoding.kasmee.util.Constants.ID_REPEATING
import com.dicoding.kasmee.util.Constants.NOTIFICATION_CHANNEL_ID
import com.dicoding.kasmee.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.dicoding.kasmee.util.Constants.NOTIFICATION_ID
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@DelicateCoroutinesApi
@AndroidEntryPoint
class TransactionReminder : BroadcastReceiver() {

    @Inject
    lateinit var repository: KasmeeRepository

    override fun onReceive(context: Context, intent: Intent) {
        intent.action
        GlobalScope.launch(Dispatchers.IO) {
            val currentTime = Calendar.getInstance().time
            val dateFormat = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            val today = dateFormat.format(currentTime)

            val result = repository.getTodayTransaction(today)
            val transaction = result.data

            if (transaction?.createdAt.isNullOrEmpty()) {
                showNotification(context)
            }
        }
    }

    fun setReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 20)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val intent = Intent(context, TransactionReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    fun cancelReminder(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, TransactionReminder::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, ID_REPEATING, intent, 0)

        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    private fun showNotification(context: Context) {
        val intent = Intent(context, MainActivity::class.java)

        val pendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(intent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(context.getString(R.string.notify_title))
                .setContentText(context.getString(R.string.notify_content))
                .setContentIntent(pendingIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            )

            notification.setChannelId(NOTIFICATION_CHANNEL_ID)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(NOTIFICATION_ID, notification.build())
    }
}