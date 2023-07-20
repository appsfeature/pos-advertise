package com.posadvertise.screensaver.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.view.Window
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.screensaver.logSS
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference

const val APPLIED_CLASS =  "MainActivity"

class TimeOutHandler(application: Application) : Application.ActivityLifecycleCallbacks {
    private var job: Job? = null
    private var callback: POSAdvertiseCallback.TimeOutCallback? = null
    private var currentActivity : WeakReference<Activity>? = null
    private var isLifecycleRegistered : Boolean = false
    private var isValidScreenSaverList = false

    init {
        registerLifecycleListener(application)
    }

    private fun registerLifecycleListener(application: Application) {
        if(!isLifecycleRegistered) {
            isLifecycleRegistered = true
            logSS("registerActivityLifecycleCallbacks : success")
            application.registerActivityLifecycleCallbacks(this)
        }else{
            logSS("registerActivityLifecycleCallbacks : failed")
        }
    }

    fun setUserInteractionCallbackIfNotInitialize() {
        logSS("setUserInteractionCallbackIfNotInitialize")
        setUserInteractionCallback(currentActivity?.get())
    }


    fun setUserInteractionCallbackOnDialog(window: Window?, activity: Activity?) {
        logSS("setUserInteractionCallbackOnDialog")
        setUserInteractionCallback(window, activity)
    }

    fun unregisterScreenSaver() {
        callback = null
        currentActivity = null
        job?.cancel()
    }

    fun addScreenSaverCallback(callback: POSAdvertiseCallback.TimeOutCallback) {
        this.callback = callback
    }

    fun removeScreenSaverCallback() {
        this.callback = null
    }

    fun resetScreenTimeOutTask(context: Context) {
        if (isValidScreenSaverList) {
            callback?.let {
                logSS("resetScreenTimeOutTask : success")
                job?.cancel()
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(POSScreenSaver.getConfigScreenTimeout(context))
                    if (isChargerConnected(context)) {
                        if(!POSAdvertise.isScreenSaverFreeze) {
                            logSS("Screen Saver Displayed after : ${POSScreenSaver.getConfigScreenTimeout(context)}")
                            it.onInActivityFound()
                        }else{
                            logSS("Screen Saver is Freeze")
                        }
                    } else {
                        logSS("Charger not connected!")
                    }
                }
            }
        }else{
            logSS("resetScreenTimeOutTask : failed")
            logSS("isValidScreenSaverList:$isValidScreenSaverList")
            job?.cancel()
        }
    }

    private fun isChargerConnected(activity: Context): Boolean {
        if(POSScreenSaver.isEnableShowWhenChargerConnected()) {
            return ScreenSaverUtil.isChargerConnected(activity);
        }else{
            return true
        }
    }

    private fun isValidAllConditions(): Boolean {
        isValidScreenSaverList = POSScreenSaver.isValidScreenSavers()
        if(POSScreenSaver.isEnableScreenSaver()){
            if(isValidScreenSaverList){
                logSS("isValidDateTimeToShow = true")
                return true
            }else{
                logSS("Start or End Date Expired.")
            }
        }else{
            logSS("isEnableScreenSaver : false")
        }
        return false
    }

    override fun onActivityCreated(activity: Activity, p1: Bundle?) {
        logSS("onActivityCreated")
        if(currentActivity == null && isValidActivityToShow(activity)) {
            this.currentActivity = WeakReference<Activity>(activity)
        }
        setUserInteractionCallback(activity)
    }

    private fun setUserInteractionCallback(activity: Activity?) {
        activity?.let {
            if(isValidActivityToShow(it) && isValidAllConditions()) {
                resetScreenTimeOutTask(it)
                it.window.callback = UserInteractionCallback(it) {
                    logSS("UI interacted")
                    resetScreenTimeOutTask(it)
                }
            }
        }
    }

    private fun setUserInteractionCallback(window: Window?, activity: Activity?) {
        activity?.let {
            if(isValidActivityToShow(it) && isValidAllConditions()) {
                resetScreenTimeOutTask(it)
                window?.callback = UserInteractionCallback(window) {
                    logSS("UI interacted")
                    resetScreenTimeOutTask(it)
                }
            }
        }
    }

    private fun isValidActivityToShow(activity: Activity): Boolean {
        return activity.javaClass.simpleName == APPLIED_CLASS
    }

    override fun onActivityStarted(p0: Activity) {}

    override fun onActivityResumed(p0: Activity) {}

    override fun onActivityPaused(p0: Activity) {}

    override fun onActivityStopped(p0: Activity) {}

    override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}

    override fun onActivityDestroyed(p0: Activity) {}



//    var currentTime: Long = System.currentTimeMillis()
//    private fun isValidTimeToShow(): Boolean {
//        return System.currentTimeMillis() > currentTime + getConfigScreenTimeout()
//    }
//    private fun isValidTimeToShow(): Boolean {
//        return System.currentTimeMillis() > currentTime + getConfigScreenTimeout()
//    }
//
//    private fun resetTimeOutTask() {
//        currentTime = System.currentTimeMillis()
//        val isValid = isValidTimeToShow()
//        if(isValid){
//            callback?.onInActivityFound()
//        }
//    }
}