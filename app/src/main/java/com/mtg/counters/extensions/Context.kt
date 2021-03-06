package com.mtg.counters.extensions

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

fun Context?.isNetworkEnable(): Boolean {
    this?.apply {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        } else {
            val networkCapabilities = connectivityManager.activeNetworkInfo
            networkCapabilities != null && networkCapabilities.isConnectedOrConnecting
        }
    }
    return false
}

fun Context?.isConnectedToWiFi(): Boolean {
    this?.apply {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            (networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                    && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
        } else {
            val networkCapabilities = connectivityManager.activeNetworkInfo
            networkCapabilities != null && networkCapabilities.type == ConnectivityManager.TYPE_WIFI
        }
    }
    return false
}