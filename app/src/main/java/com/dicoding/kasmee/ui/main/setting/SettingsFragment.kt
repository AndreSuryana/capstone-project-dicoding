package com.dicoding.kasmee.ui.main.setting

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.kasmee.R
import com.dicoding.kasmee.ui.notification.TransactionReminder
import kotlinx.coroutines.DelicateCoroutinesApi

@DelicateCoroutinesApi
class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        prefNotification?.setOnPreferenceChangeListener { _, _ ->
            val reminder = TransactionReminder()

            if (prefNotification.isChecked) {
                reminder.cancelReminder(requireContext())
            } else {
                reminder.setReminder(requireContext())
            }

            true
        }
    }
}