package com.example.ana.presentation.utills

import android.content.Context
import android.view.View
import androidx.core.content.ContextCompat
import com.example.ana.R
import com.google.android.material.snackbar.Snackbar

fun showActionSnackbar(
    view: View,
    message: String,
    actionTitle: String,
    action: () -> Unit,
    context: Context
) {
    Snackbar.make(view, message, Snackbar.LENGTH_LONG).setAction(actionTitle) {
        action()
    }.setActionTextColor(ContextCompat.getColor(context, R.color.black)).show()
}