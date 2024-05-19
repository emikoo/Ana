package com.example.ana.presentation.extensions

import android.content.Context
import android.content.res.Configuration
import androidx.fragment.app.FragmentManager
import androidx.preference.PreferenceManager
import com.example.ana.data.local.PrefsSettings
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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