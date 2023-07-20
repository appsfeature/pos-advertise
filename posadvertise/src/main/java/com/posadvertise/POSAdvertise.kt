package com.posadvertise

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.gson.reflect.TypeToken
import com.helper.util.BaseConstants
import com.helper.util.BaseUtil
import com.helper.util.GsonParser
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

    private var mTerminalName: String = "X990"
    var isScreenSaverFreeze: Boolean = false
    var httpsTutorialUrl: String? = null
    var isDebugMode: Boolean = false
    var posScreenSaver: POSScreenSaver? = null
    var posBanner : POSBanner? = null
    var posTutorials : POSTutorials? = null


    fun getAdvertiseVersionCode(context: Context?): Int {
        return POSAdvertisePreference.getAdvertiseVersionCode(context)
    }

//    fun setAdvertiseVersionCode(context: Context?, value: Int) {
//        POSAdvertisePreference.setAdvertiseVersionCode(context, value)
//    }

    fun showBanner(activity: AppCompatActivity, container: FrameLayout) {
        val property = ExtraProperty(bannerType = BannerType.Both)
        posBanner?.show(activity.lifecycle, container, property)
    }

    fun showBannerStatic(fragment: Fragment, container: FrameLayout) {
        val property = ExtraProperty(bannerType = BannerType.Local, viewType = BannerViewType.ALL)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment.lifecycle, container, property)
            }
        })
    }

    fun showBannerOnHomeScreen(fragment: Fragment, container: FrameLayout?) {
        val property = ExtraProperty(isActionPerformed = true, bannerType = BannerType.Both, viewType = BannerViewType.HOME, isBannerIndicatorTop = true)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment.lifecycle, container, property)
            }
        })
    }

    fun showBannerOnVoidScreen(fragment: Fragment, container: FrameLayout) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.VOID)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment.lifecycle, container, property)
            }
        })
    }

    fun showBannerOnEmiSale(fragment: Fragment, container: FrameLayout) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.EMI_SALE)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment.lifecycle, container, property)
            }
        })
    }

    fun showBannerOnBrandEmi(activity: AppCompatActivity, container: FrameLayout) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.BRAND_EMI)
        posBanner?.show(activity.lifecycle, container, property)
    }

    fun showBannerOnDynamicQR(fragment: Fragment, container: FrameLayout) {
        val property = ExtraProperty(isActionPerformed = false, bannerType = BannerType.Both, viewType = BannerViewType.DYNAMIC_QR)
        addLiveChangeListener(fragment, object : OnAdvertiseUpdate{
            override fun onUIUpdate() {
                posBanner?.show(fragment.lifecycle, container, property)
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

//    fun registerScreenSaver(context: Context) {
//        this.isScreenSaverFreeze = false
//        resetPOSAdvertiseProperty(context)
//        POSScreenSaver.registerScreenSaver(context)
//    }
//
//    fun unregisterScreenSaver() {
//        this.isScreenSaverFreeze = true
//        POSScreenSaver.unregisterScreenSaver()
//    }

    fun openTutorial(activity: Activity, title: String) {
        activity.startActivity(Intent(activity, TutorialsActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, ExtraProperty())
            .putExtra(BaseConstants.TITLE, title))
    }

//    fun openTutorialFragmentById(activity: Activity, title: String, videoId : Int) {
//        val property = ExtraProperty().apply {
//            this.title = title
//            this.model = POSTutorials.getVideoById(videoId)
//        }
//        if(property.model != null) {
//            val fragment = TutorialGifFragment().apply {
//                arguments = POSAdvertiseUtility.getBundle(property)
//            }
//            if (activity is AppCompatActivity) {
//                val trans = activity.supportFragmentManager.beginTransaction().apply {
//                    add(R.id.content, fragment)
//                    addToBackStack(fragment.javaClass.simpleName)
//                }
//                trans.addToBackStack(null)
//                trans.commitAllowingStateLoss()
//            }
//        }else{
//            Toast.makeText(activity, "Tutorial not found", Toast.LENGTH_SHORT).show()
//        }
//    }

    fun openTutorialById(activity: Activity, title: String, videoId : Int) {
        val property = ExtraProperty().apply {
            this.title = title
            this.model = POSTutorials.getVideoById(videoId)
        }
        activity.startActivity(Intent(activity, TutorialsActivity::class.java)
            .putExtra(BaseConstants.EXTRA_PROPERTY, property))
    }

    fun init(
        application: Application,
        versionName: String,
        callback: POSAdvertiseCallback.Callback<Boolean>
    ) {
        if(!POSAdvertisePreference.isInternalZipFileExtracted(application, versionName)){
            POSAdvertiseDataManager.extractLocalZipFile(application, object : POSAdvertiseCallback.Callback<Boolean>{
                override fun onSuccess(response: Boolean) {
                    POSAdvertisePreference.setInternalZipFileExtracted(application, versionName, true)
                    POSScreenSaver.setUserInteractionCallbackIfNotInitialize()
                    callback.onSuccess(response)
                }

                override fun onFailure(e: Exception?) {
                    super.onFailure(e)
                    POSAdvertisePreference.setInternalZipFileExtracted(application, versionName, true)
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

    var isBannerUpdateInProgress = false

    fun downloadFileFromServer(context: Context, callback: POSAdvertiseCallback.Download<Boolean>) {
        if(!isBannerUpdateInProgress) {
            isBannerUpdateInProgress=true

            val isUpdateAvailable = POSAdvertisePreference.isUpdateAvailable(context)
            val urlData = POSAdvertisePreference.getUpdateUrlData(context)
            if(isUpdateAvailable && !urlData.isNullOrEmpty()) {
                val urlDataMap = GsonParser.fromJson(urlData,
                    object : TypeToken<MutableList<String>>() {}
                )
                val baseUrl = urlDataMap[2]
                val ftpIPPort = urlDataMap[3]
                val fileName = urlDataMap[7]
//              val filesize = urlDataMap[8]
//                val finalUrl = baseUrl.replace("/app/", ":$ftpIPPort") + "/app/${getTerminalName()}/${fileName}"
                val finalUrl = BaseUtil.getValidUrl(baseUrl, ftpIPPort, fileName, getTerminalName())
                CoroutineScope(Dispatchers.Main).launch {
                    callback.onProgressStart()
                }

                downloadFileFromServer(context, finalUrl, callback)
            }else{
                isBannerUpdateInProgress = false
            }
        }

    }

    fun getTerminalName(): String {
        return mTerminalName
    }

    fun setTerminalName(terminalName : String): POSAdvertise {
        this.mTerminalName = terminalName
        return this
    }


    fun downloadFileFromServer(context: Context, downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>) {
        POSAdvertiseDataManager.downloadUpdate(context, downloadFileUrl, object : POSAdvertiseCallback.Download<Boolean> {
            override fun onSuccess(response: Boolean) {
                callback.onProgressEnd()
                logAdv("syncPOSAdvertise: onSuccess")
                onDownloadCompletedUpdateUi()
                callback.onSuccess(true)
            }

            override fun onProgressUpdate(progress: Int) {
                callback.onProgressUpdate(progress)
            }

            override fun onFailure(e: Exception?) {
                super.onFailure(e)
                callback.onProgressEnd()
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
        POSScreenSaver.startScreenSaverFreeze(activity)
    }

    fun startScreenSaverFreeze(fragment: Fragment) {
        POSScreenSaver.startScreenSaverFreeze(fragment)
    }

    private var screenSaverForceDismissListener: POSAdvertiseCallback.ScreenSaverForceDismissListener? = null

    fun setScreenSaverForceDismissListener(screenSaverForceDismissListener: POSAdvertiseCallback.ScreenSaverForceDismissListener) {
        this.screenSaverForceDismissListener = screenSaverForceDismissListener
    }
    fun getScreenSaverForceDismissListener() : POSAdvertiseCallback.ScreenSaverForceDismissListener?{
        return screenSaverForceDismissListener
    }

}