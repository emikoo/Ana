package com.example.ana.presentation.utills

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.view.LayoutInflater
import com.example.ana.R

class ProgressDialog {
    companion object {
        fun progressDialog(context: Context): Dialog {
            val dialog = Dialog(context)
            val inflate =
                LayoutInflater.from(context).inflate(R.layout.progress_dialog , null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window !!.setBackgroundDrawable(
                ColorDrawable(Color.TRANSPARENT)
            )
            return dialog
        }
    }
}

fun checkInternetConnection(connectedAction: () -> Unit , context: Context , noInternetAction: () -> Unit) {
    if (internetIsConnected(context)) {
        connectedAction()
    } else {
        noInternetAction()
    }
}

fun internetIsConnected(context: Context): Boolean {
    var connected = false
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    connected =
        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)!!.state == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)!!.state == NetworkInfo.State.CONNECTED
    return connected
}