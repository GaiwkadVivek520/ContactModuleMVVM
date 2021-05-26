package com.vvek.consentmoduleapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log

// This is System BroadcastReceiver to check Internet connectivity. If network available consent flag is true. This is
// Parallel flow for Contact list.
class ConnectivityReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        Log.d("BROADCAST", "network change")
        if (isNetworkAvailable) {
            setConsentFlag(context, true)
        } else {
            setConsentFlag(context, false)
        }
    }

    private val isNetworkAvailable: Boolean
        private get() = false

    private fun setConsentFlag(context: Context, flag: Boolean) {
        val sharedPref: SharedPreferences = context.getSharedPreferences(
            "myPrefs",
            Context.MODE_PRIVATE
        )

        val editor = sharedPref.edit()
        editor.putBoolean("flag", !flag)
        editor.apply()
    }

}