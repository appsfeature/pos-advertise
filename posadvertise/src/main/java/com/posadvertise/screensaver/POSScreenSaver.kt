package com.posadvertise.screensaver

import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import com.helper.util.BaseConstants
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.screensaver.model.ScreenSaverProperty
import com.posadvertise.screensaver.util.ScreenSaverUtil
import com.posadvertise.screensaver.util.TimeOutHandler
import com.posadvertise.screensaver.view.ScreenSaverActivity
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.ExtraProperty

fun logSS(message: String){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSScreenSaver::class.java.simpleName, message)
    }
}

object POSScreenSaver {

    private var timeOutHandler: TimeOutHandler? = null
    private var screenTimeOut: Long? = null
    var property: ScreenSaverProperty? = null

    fun init(application: Application) {
        ScreenSaverUtil.setScreenOffTimeOut(application)
        timeOutHandler = TimeOutHandler(application)
        initScreenSaver(application)
    }

    private fun initScreenSaver(context: Context) {
        timeOutHandler?.addScreenSaverCallback(object : POSAdvertiseCallback.TimeOutCallback{
            override fun onInActivityFound() {
                if(isValidScreenSavers()) {
                    timeOutHandler?.removeScreenSaverCallback()
                    startScreenSaver(context)
                }
            }
        })
    }

    // reset all singleton variables in this class
    fun resetAttributes(context: Context?) {
        screenTimeOut = null
    }

    fun setUserInteractionCallbackIfNotInitialize() {
        timeOutHandler?.setUserInteractionCallbackIfNotInitialize()
    }


    fun registerScreenSaver(context: Context) {
        initScreenSaver(context)
    }

    fun unregisterScreenSaver() {
        timeOutHandler?.unregisterScreenSaver()
    }

    fun reSetScreenSaver(context: Context) {
        initScreenSaver(context)
        timeOutHandler?.resetScreenTimeOutTask(context)
    }

    private fun startScreenSaver(context: Context) {
        val intent = Intent(context, ScreenSaverActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, ExtraProperty())
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        context.startActivity(intent)
    }

    fun isEnableShowWhenChargerConnected(): Boolean {
        return property?.isEnableShowWhenChargerConnected ?: true
    }

    fun getConfigScreenTimeout(context: Context): Long {
        if (screenTimeOut == null) {
            val configTimeOut =
                property?.screenTimeOut ?: POSAdvertiseConstants.DefaultScreenTimeOut
            val systemScreenTimeOut = POSAdvertiseUtility.getSystemScreenOffTimeOut(context)
            screenTimeOut = if (configTimeOut < systemScreenTimeOut) {
                configTimeOut
            } else {
                systemScreenTimeOut - 2000
            }
            logSS("ScreenTimeout:$screenTimeOut")
        }
        return screenTimeOut!!
    }

    private fun isValidDate(startDate : String?, endDate : String?): Boolean {
        return (System.currentTimeMillis() > POSAdvertiseUtility.getTimeInMillis(startDate)
                && System.currentTimeMillis() < POSAdvertiseUtility.getTimeInMillis(endDate))
    }

    fun isValidScreenSavers(): Boolean {
        val mList = property?.list
        mList?.let {
            for (item in mList){
                if(isValidDate(item.startDate, item.endDate)){
                    return true
                }
            }
        }
        return false
    }

    fun getValidScreenSavers(): MutableList<AdvertiseModel> {
        val finalList: MutableList<AdvertiseModel> = mutableListOf()
        val mList = property?.list
        mList?.let {
            for (item in mList){
                if(isValidDate(item.startDate, item.endDate)){
                    finalList.add(item)
                }
            }
        }
        return finalList
    }

    fun resetScreenTimeOutTask(context: Context) {
        timeOutHandler?.resetScreenTimeOutTask(context)
    }

    fun isEnableScreenSaver(): Boolean {
        return property?.isEnableScreenSaver ?: true
    }

}