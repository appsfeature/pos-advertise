package com.posadvertise.screensaver.util

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.screensaver.logSS


object ScreenSaverUtil {

    fun enableFullBrightness(activity: Context) {
        //@UpdateLater
    }

    fun setScreenOffTimeOut(context: Context) {
        try {
            POSScreenSaver.property?.let {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(Settings.System.canWrite(context)){
                        Settings.System.putInt(
                            context.contentResolver,
                            Settings.System.SCREEN_OFF_TIMEOUT,
                            it.screenTimeOut.toInt()
                        )
                    }
                }
//                val mSystemScreenOffTimeOut =
//                    Settings.System.getInt(
//                        context.contentResolver,
//                        Settings.System.SCREEN_OFF_TIMEOUT
//                    )
            }
        } catch (e: Exception) {
            logSS(e.toString())
        }
    }

    fun isChargerConnected(context: Context?):Boolean{
//        if(BuildConfig.DEBUG){
//            return true
//        }
        val batteryStatus: Intent? =
            IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
                context?.registerReceiver(null, ifilter)
            }
        val status: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_STATUS, -1) ?: -1
        var isCharging: Boolean = status == BatteryManager.BATTERY_STATUS_CHARGING
                || status == BatteryManager.BATTERY_STATUS_FULL
        val chargePlug: Int = batteryStatus?.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1) ?: -1
        val usbCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_USB
        val acCharge: Boolean = chargePlug == BatteryManager.BATTERY_PLUGGED_AC
        when {
            usbCharge -> {

                isCharging=true
            }
            acCharge -> {
                isCharging=true
            }

            else -> {
                isCharging=false
            }
        }
        return isCharging
    }
}