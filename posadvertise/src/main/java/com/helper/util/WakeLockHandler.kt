package com.helper.util

import android.content.Context
import android.os.PowerManager
import androidx.appcompat.app.AppCompatActivity

class WakeLockHandler(context: Context, tag : String) {

    private var wakeLock: PowerManager.WakeLock?

    init {
        val powerManager = context.getSystemService(AppCompatActivity.POWER_SERVICE) as PowerManager
        wakeLock = powerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK or PowerManager.ACQUIRE_CAUSES_WAKEUP, tag)
    }

    fun startWakeLock(): WakeLockHandler {
        wakeLock?.acquire()
        return this
    }

    fun resumeWakeLock() {
        wakeLock?.let {
            if(!it.isHeld) {
                it.acquire()
            }
        }
    }


    fun stopWakeLock() {
        wakeLock?.let {
            if(it.isHeld){
                it.release()
            }
        }
    }
}