package com.example.ana.data.local

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class PrefsSettings(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private var prefsEditor: SharedPreferences.Editor = prefs.edit()
    private val UID = "UID"
    private val LANGUAGE = "LANGUAGE"

    fun setFirstTimeLaunch(isFirstTime: Int) {
        prefsEditor.putInt(IS_FIRST_TIME_LAUNCH, isFirstTime).commit()
    }

    fun isFirstTimeLaunch(): Int = prefs.getInt(IS_FIRST_TIME_LAUNCH, FIRST_TIME)

    fun saveSettingsLanguage(language: String?) {
        prefsEditor.putString(LANGUAGE, language).apply()
    }

    fun getSettingsLanguage(): String {
        return prefs.getString(LANGUAGE, Locale.getDefault().language)
            ?: "eng"
    }

    fun saveCurrentUserId(uid: String?) {
        prefsEditor.putString(UID, uid).apply()
    }

    fun getCurrentUserId(): String {
        return prefs.getString(UID, "") ?: ""
    }

    companion object {
        private const val PREFS_NAME = "Ana"
        const val FIRST_TIME = 1
        const val NOT_AUTH = 2
        const val USER = 3
        private const val IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch"
    }
}