package com.example.bioreactor

import android.os.SystemClock

object UptimeUtil {
    fun getUptime(): String {
        val elapsedMillis = SystemClock.elapsedRealtime()

        val seconds = (elapsedMillis / 1000 % 60).toInt()
        val minutes = (elapsedMillis / (1000 * 60) % 60).toInt()
        val hours = (elapsedMillis / (1000 * 60 * 60) % 24).toInt()
        val days = (elapsedMillis / (1000 * 60 * 60 * 24)).toInt()

        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds)
    }
}
