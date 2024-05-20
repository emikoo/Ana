package com.example.ana.presentation.extensions

import android.content.Context
import android.content.res.Configuration
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.example.ana.data.local.PrefsSettings
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

fun BottomSheetDialogFragment.show(fragmentManager: FragmentManager?) {
    val bottomSheetDialogFragment: BottomSheetDialogFragment = this
    fragmentManager?.let {
        bottomSheetDialogFragment.show(
            it ,
            bottomSheetDialogFragment.tag
        )
    }
}

fun PreferenceManager.updateLanguage(lang: String, context: Context, prefsSettings: PrefsSettings) {
    val locale = Locale(lang)
    Locale.setDefault(locale)
    val config = Configuration()
    config.locale = locale
    context.resources.updateConfiguration(config, context.resources.displayMetrics)
    prefsSettings.saveSettingsLanguage(lang)
}

fun String.calculateWeeksLived(): Double {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val birthDate = LocalDate.parse(this, formatter)
    val currentDate = LocalDate.now()
    val daysLived = ChronoUnit.DAYS.between(birthDate, currentDate)

    return daysLived / 7.0
}

fun TextView.ageOfChild(birthdate: String) {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    val birthDate = LocalDate.parse(birthdate, formatter)
    val currentDate = LocalDate.now()
    val birthday = Period.between(birthDate, currentDate)
    this.text = "${birthday.years} year, ${birthday.months} months, ${birthday.days} days"
}