package com.dicoding.kasmee.util

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.dicoding.kasmee.R
import com.dicoding.kasmee.ui.main.MainActivity
import com.dicoding.kasmee.util.Constants.USER_TOKEN
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.user_token), Context.MODE_PRIVATE)

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun checkAuthToken(context: Context) {
        if (fetchAuthToken() != null) {
            Intent(context, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                context.applicationContext.startActivity(it)
            }
        }
    }

    fun clearToken() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}