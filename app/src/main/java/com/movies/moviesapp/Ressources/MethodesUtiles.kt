package com.movies.moviesapp.Ressources

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

object MethodesUtiles {
    fun isInternetAvailable(mContext: Context): Boolean {
        var haveConnectedWifi = false
        var haveConnectedMobile = false
        val cm = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.allNetworkInfo
        for (ni in netInfo) {
            if (ni.typeName.equals(
                    "WIFI",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedWifi = true
            if (ni.typeName.equals(
                    "MOBILE",
                    ignoreCase = true
                )
            ) if (ni.isConnected) haveConnectedMobile = true
        }
        return haveConnectedWifi || haveConnectedMobile
    }
}