package com.posadvertise

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
import com.posadvertise.POSAdvertiseCallback.OnAdvertiseUpdate
import com.posadvertise.banner.POSBanner
import com.posadvertise.banner.model.BannerType
import com.posadvertise.banner.model.BannerViewType
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.tutorials.POSTutorials
import com.posadvertise.tutorials.view.TutorialsActivity
import com.posadvertise.util.POSAdvertiseDataManager
import com.posadvertise.util.POSAdvertisePreference
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.ExtraProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun logAdv(message: String){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSAdvertise::class.java.simpleName, message)
    }
}

fun logAdv(tag: String, message: String){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSAdvertise::class.java.simpleName, tag + message)
    }
}

object POSAdvertise {

    var isScreenSaverFreeze: Boolean = false
    var httpsTutorialUrl: String? = null
    var isDebugMode: Boolean = false
    var posScreenSaver: POSScreenSaver? = null
    var posBanner : POSBanner? = null
    var posTutorials : POSTutorials? = null

    fun showBanner(activity: Activity, container: Int) {
        val property = ExtraProperty(bannerType = BannerType.Both)
        posBanner?.show(activity, container, property)
    }

    fun showBannerStatic(fragment: Fragment, container: Int) {
        val property = ExtraProperty(bannerType = BannerType.Local, viewType = BannerViewType.ALL)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment, container, property)
            }
        })
    }

    fun showBannerOnHomeScreen(fragment: Fragment, container: Int) {
        val property = ExtraProperty(isActionPerformed = true, bannerType = BannerType.Both, viewType = BannerViewType.HOME)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment, container, property)
            }
        })
    }

    fun showBannerOnVoidScreen(fragment: Fragment, container: Int) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.VOID)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment, container, property)
            }
        })
    }

    fun showBannerOnPinEntry(fragment: Fragment, container: Int) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.PIN_ENTRY)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment, container, property)
            }
        })
    }

    fun showBannerOnAmtEntryScreen(fragment: Fragment, container: Int) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.AMT_ENTRY)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment, container, property)
            }
        })
    }

    private fun addLiveChangeListener(fragment: Fragment, listener : OnAdvertiseUpdate) {
        listener.onUIUpdate()
        registerUIUpdateCallbacks(fragment.hashCode(), object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                listener.onUIUpdate()
            }
        })
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onDestroy(owner: LifecycleOwner) {
                super.onDestroy(owner)
                unregisterUIUpdateCallbacks(fragment.hashCode())
                owner.lifecycle.removeObserver(this)
            }
        })
    }

    fun registerScreenSaver(context: Context) {
        this.isScreenSaverFreeze = false
        resetPOSAdvertiseProperty(context)
        POSScreenSaver.registerScreenSaver(context)
    }

    fun unregisterScreenSaver() {
        this.isScreenSaverFreeze = true
        POSScreenSaver.unregisterScreenSaver()
    }

    fun openTutorial(activity: Activity, title: String) {
        activity.startActivity(Intent(activity, TutorialsActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, ExtraProperty())
            .putExtra(BaseConstants.TITLE, title))
    }

    fun openTutorialById(activity: Activity, title: String, videoId : Int) {
        val property = ExtraProperty().apply {
            this.title = title
            this.model = POSTutorials.getVideoById(videoId)
        }
        activity.startActivity(Intent(activity, TutorialsActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, property))
    }

    fun init(application: Application, callback: POSAdvertiseCallback.Callback<Boolean>) {
        if(!POSAdvertisePreference.isInternalZipFileExtracted(application)){
            POSAdvertiseDataManager.extractLocalZipFile(application, object : POSAdvertiseCallback.Callback<Boolean>{
                override fun onSuccess(response: Boolean) {
                    POSAdvertisePreference.setInternalZipFileExtracted(application, true)
                    POSScreenSaver.setUserInteractionCallbackIfNotInitialize()
                    callback.onSuccess(response)
                }

                override fun onFailure(e: Exception?) {
                    super.onFailure(e)
                    POSAdvertisePreference.setInternalZipFileExtracted(application, true)
                    callback.onFailure(e)
                }
            })
        }
    }

    private val mUIUpdateCallbacks = HashMap<Int, OnAdvertiseUpdate?>()

    fun registerUIUpdateCallbacks(hashCode: Int, callback: OnAdvertiseUpdate?) {
        synchronized(mUIUpdateCallbacks) { mUIUpdateCallbacks.put(hashCode, callback) }
    }

    fun unregisterUIUpdateCallbacks(hashCode: Int) {
        if (mUIUpdateCallbacks[hashCode] != null) {
            synchronized(mUIUpdateCallbacks) { mUIUpdateCallbacks.remove(hashCode) }
        }
    }

    fun updateUICallback() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (mUIUpdateCallbacks.size > 0) {
                    mUIUpdateCallbacks.forEach { (_, v) ->
                        logAdv("onUIUpdate()")
                        v?.onUIUpdate()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }


    private val mListenerCallbacks = HashMap<Int, POSAdvertiseCallback.OnAdvertiseListener>()

    fun addListener(hashCode: Int, callback: POSAdvertiseCallback.OnAdvertiseListener) {
        synchronized(mListenerCallbacks) { mListenerCallbacks.put(hashCode, callback) }
    }

    fun removeListener(hashCode: Int) {
        if (mListenerCallbacks[hashCode] != null) {
            synchronized(mListenerCallbacks) { mListenerCallbacks.remove(hashCode) }
        }
    }

    fun onDownloadCompletedUpdateUi() {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (mListenerCallbacks.size > 0) {
                    mListenerCallbacks.forEach { (_, v) ->
                        logAdv("onDownloadCompletedUpdateUi()")
                        v.onDownloadCompletedUpdateUi()
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
    fun onBannerItemClicked(context: Context?, item: AdvertiseModel?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (mListenerCallbacks.size > 0) {
                    mListenerCallbacks.forEach { (_, v) ->
                        logAdv("onBannerItemClicked()")
                        v.onBannerItemClicked(context, item)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }
    fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                if (mListenerCallbacks.size > 0) {
                    mListenerCallbacks.forEach { (_, v) ->
                        logAdv("onScreenSaverItemClicked()")
                        v.onScreenSaverItemClicked(context, item)
                    }
                }
            } catch (e: java.lang.Exception) {
                e.printStackTrace()
            }
        }
    }

    fun downloadFileFromServer(context: Context, downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>) {
        POSAdvertiseDataManager.downloadUpdate(context, downloadFileUrl, object : POSAdvertiseCallback.Download<Boolean> {
            override fun onSuccess(response: Boolean) {
                logAdv("syncPOSAdvertise: onSuccess")
                onDownloadCompletedUpdateUi()
                callback.onSuccess(true)
            }

            override fun onProgressUpdate(progress: Int) {
                callback.onProgressUpdate(progress)
            }

            override fun onFailure(e: Exception?) {
                super.onFailure(e)
                onDownloadCompletedUpdateUi()
                callback.onFailure(e)
                logAdv("syncPOSAdvertise: onFailure")
            }
        })
    }

    fun resetPOSAdvertiseProperty(context: Context?) {
        logAdv("resetPOSAdvertiseProperty")
        POSBanner.setProperty(context)
        POSScreenSaver.apply {
            property = POSAdvertiseUtility.getSSProperty(context)
            resetAttributes(context)
        }
        POSTutorials.property = POSAdvertiseUtility.getTutorialProperty(context)
    }

    fun startScreenSaverFreeze(activity: Activity) {
        if(activity is AppCompatActivity){
            activity.lifecycle.addObserver(object : DefaultLifecycleObserver{
                override fun onCreate(owner: LifecycleOwner) {
                    super.onCreate(owner)
                    isScreenSaverFreeze = true
                }

                override fun onDestroy(owner: LifecycleOwner) {
                    super.onDestroy(owner)
                    activity.lifecycle.removeObserver(this)
                    isScreenSaverFreeze = false
                }
            })
        }
    }

    fun startScreenSaverFreeze(fragment: Fragment) {
        fragment.lifecycle.addObserver(object : DefaultLifecycleObserver{
            override fun onCreate(owner: LifecycleOwner) {
                super.onCreate(owner)
                isScreenSaverFreeze = true
            }

            override fun onDestroy(owner: LifecycleOwner) {
                fragment.lifecycle.removeObserver(this)
                isScreenSaverFreeze = false
                POSScreenSaver.resetScreenTimeOutTask(fragment.requireContext())
                super.onDestroy(owner)
            }
        })
    }
}