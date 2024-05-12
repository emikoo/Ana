package com.example.ana.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class PrefsSettings(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME , Context.MODE_PRIVATE)
    private var prefsEditor: SharedPreferences.Editor = prefs.edit()
    private val UID = "UID"
    private val PHONE_NUMBER = "PHONE_NUMBER"
    private val NAME = "NAME"

    fun setFirstTimeLaunch(isFirstTime: Int) {
        prefsEditor.putInt(IS_FIRST_TIME_LAUNCH , isFirstTime).commit()
    }

    fun isFirstTimeLaunch(): Int = prefs.getInt(IS_FIRST_TIME_LAUNCH , FIRST_TIME)

    fun saveCurrentUserId(uid: String?) {
        prefsEditor.putString(UID , uid).apply()
    }

    fun getCurrentUserId(): String {
        return prefs.getString(UID , "") ?: ""
    }

    fun savePhoneNumber(number: String?) {
        prefsEditor.putString(PHONE_NUMBER, number).apply()
    }

    fun getPhoneNumber(): String {
        return prefs.getString(PHONE_NUMBER, "") ?: ""
    }

    fun saveName(name: String?) {
        prefsEditor.putString(NAME, name).apply()
    }

    fun getName() : String {
        return prefs.getString(NAME, "") ?: ""
    }
    companion object {
        private const val PREFS_NAME = "Ana"
        const val FIRST_TIME = 1
        const val NOT_AUTH = 2
        const val USER = 3
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }
}