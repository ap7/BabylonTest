package com.ghostwan.babylontest.ui.util

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.wifi.WifiManager
import android.view.View
import com.ghostwan.babylontest.R
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber
import java.net.UnknownHostException

fun handleError(context: Context, view: View, throwable: Throwable) {
    Timber.e(throwable)
    when(throwable) {
        is UnknownHostException -> {
            val wifi = context.getSystemService(Context.WIFI_SERVICE) as WifiManager
            if(!wifi.isWifiEnabled) {
                Snackbar
                    .make(view, R.string.network_error, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.wifi_activation) { wifi.isWifiEnabled = true }
                    .show()
            }
            else {
                Snackbar
                    .make(view, R.string.network_error, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.wifi_settings) {
                        val intent = Intent(WifiManager.ACTION_PICK_WIFI_NETWORK)
                        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(intent);
                    }
                    .show()
            }
        }
        else -> {
            Snackbar.make(view, R.string.error, Snackbar.LENGTH_SHORT).show()
        }
    }

}