package com.posadvertise.screensaver

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
                    startScreenSaverFreeze(context)
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

    fun onStop() {
    }

    fun resetScreenTimeout(context: Context) {
        initScreenSaver(context)
        timeOutHandler?.resetScreenTimeOutTask(context)
    }

    private fun startScreenSaverFreeze(context: Context) {
        val intent = Intent(context, ScreenSaverActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, ExtraProperty())
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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


    fun startScreenSaverFreeze(activity: Activity) {
        if(activity is AppCompatActivity){
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
                override fun onCreate(owner: LifecycleOwner) {
                    super.onCreate(owner)
                    POSAdvertise.isScreenSaverFreeze = true
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    activity.lifecycle.removeObserver(this)
                    POSAdvertise.isScreenSaverFreeze = false
                    resetScreenTimeOutTask(activity)
                }
            })
        }
    }

    fun startScreenSaverFreeze(fragment: Fragment) {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                applyFreeze(fragment.requireContext(), true)
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                applyFreeze(fragment.requireContext(), false)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.lifecycle.removeObserver(this)
                super.onDestroy(owner)
            }
        })
    }

    fun startScreenSaver(fragment: Fragment) {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver {

            override fun onStart(owner: LifecycleOwner) {
                super.onStart(owner)
                CoroutineScope(Dispatchers.Main).launch {
                    delay(400)
                    applyFreeze(fragment.requireContext(), false)
                }
            }

            override fun onStop(owner: LifecycleOwner) {
                super.onStop(owner)
                applyFreeze(fragment.requireContext(), true)
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.lifecycle.removeObserver(this)
                super.onDestroy(owner)
            }
        })
    }

    private fun applyFreeze(context: Context, isFreeze: Boolean) {
        if(isFreeze) {
            logSS("isScreenSaverFreeze = true")
            POSAdvertise.isScreenSaverFreeze = true
        }else{
            logSS("isScreenSaverFreeze = false")
            POSAdvertise.isScreenSaverFreeze = false
            resetScreenTimeOutTask(context)
        }
    }

}