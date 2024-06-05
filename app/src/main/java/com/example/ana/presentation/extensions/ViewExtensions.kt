package com.example.ana.presentation.extensions

import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.example.ana.R
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
            it,
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
    val year = birthday.years.toString() + context.getString(R.string.year)
    val month = birthday.months.toString() + context.getString(R.string.month)
    val days = birthday.days.toString() + context.getString(R.string.days)
    this.text = "$year $month $days"
}
fun Fragment.isKeyboardVisible(): Boolean {
    val rootView: View = requireActivity().findViewById(android.R.id.content)
    val rect = Rect()
    rootView.getWindowVisibleDisplayFrame(rect)
    val screenHeight = rootView.height
    val keypadHeight = screenHeight - rect.bottom
    return keypadHeight > screenHeight * 0.15
}