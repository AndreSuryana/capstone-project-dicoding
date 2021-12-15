package com.dicoding.kasmee.ui.main.setting

import android.os.Bundle
import android.util.Log
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.dicoding.kasmee.R
import com.dicoding.kasmee.ui.notification.TransactionWorker
import com.dicoding.kasmee.util.Constants.DAILY_UNIQUE_WORK
import com.dicoding.kasmee.util.Constants.TARGET_UNIQUE_WORK
import java.util.*
import java.util.concurrent.TimeUnit

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val prefDailyReminderNotification =
            findPreference<ListPreference>(getString(R.string.pref_key_notify_daily_reminder))
        prefDailyReminderNotification?.setOnPreferenceChangeListener { _, newValue ->
            val selectedTime = newValue.toString()
            Log.d("Selected", selectedTime)

            if (selectedTime != getString(R.string.pref_notify_off)) {
                setTime(selectedTime)
            } else {
                WorkManager.getInstance(requireContext()).cancelUniqueWork(DAILY_UNIQUE_WORK)
            }

            true
        }

        val prefTargetReminderNotification =
            findPreference<SwitchPreference>(getString(R.string.pref_key_notify_target_reminder))
        prefTargetReminderNotification?.setOnPreferenceChangeListener { _, _ ->
            val workManager = WorkManager.getInstance(requireContext())

            if (prefTargetReminderNotification.isChecked) {
                workManager.cancelUniqueWork(TARGET_UNIQUE_WORK)
            }

            true
        }
    }

    private fun setTime(time: String) {
        val hour = time.toInt()
        val currentDate = Calendar.getInstance()
        val dueDate = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        if (dueDate.before(currentDate)) {
            dueDate.add(Calendar.HOUR_OF_DAY, 24)
        }

        val timeDiff = dueDate.timeInMillis - currentDate.timeInMillis

        val dailyWorkRequest =
            PeriodicWorkRequest.Builder(TransactionWorker::class.java, 1, TimeUnit.DAYS)
                .setInitialDelay(timeDiff, TimeUnit.MILLISECONDS)
                .build()

        WorkManager.getInstance(requireContext())
            .enqueueUniquePeriodicWork(
                DAILY_UNIQUE_WORK,
                ExistingPeriodicWorkPolicy.REPLACE,
                dailyWorkRequest
            )
    }
}