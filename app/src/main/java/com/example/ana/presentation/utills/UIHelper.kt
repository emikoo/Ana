package com.example.ana.presentation.utills

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.ana.R
import com.google.android.material.snackbar.Snackbar

fun showActionSnackbar(
    view: View,
    message: String,
    actionTitle: String,
    action: () -> Unit,
    context: Context,
    cancelAction: () -> Unit
) {
    Snackbar.make(view, message, Snackbar.LENGTH_SHORT)
        .setAction(actionTitle) {
            action()
        }
        .setActionTextColor(ContextCompat.getColor(context, R.color.purple))
        .addCallback(object : Snackbar.Callback() {
            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                if (event != DISMISS_EVENT_ACTION) {
                    cancelAction()
                }
            }
        })
        .setDuration(700)
        .show()
}