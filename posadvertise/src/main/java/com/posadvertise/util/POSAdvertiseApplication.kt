package com.posadvertise.util


import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.helper.util.GsonParser
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.POSBanner
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.banner.model.BannerType
import com.posadvertise.logAdv
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.screensaver.model.ScreenSaverProperty
import com.posadvertise.tutorials.POSTutorials
import com.posadvertise.tutorials.model.TutorialProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class POSAdvertiseApplication : Application(), POSAdvertiseCallback.OnAdvertiseListener {

    private var posAdvertise : POSAdvertise? = null

    abstract fun isDebugMode() : Boolean;

    abstract fun getVersionName() : String;

    override fun onCreate() {
        super.onCreate()
//        //call this method for re-extract local zip file.
//        POSAdvertisePreference.setInternalZipFileExtracted(this, false)
        initLibs(getVersionName())

        if (POSAdvertise.isDebugMode) {
            getTempLocalJson()
        }
    }

    private fun getTempLocalJson() {
        CoroutineScope(Dispatchers.IO).launch {
            logAdv("Banners : ${GsonParser.toJson(POSAdvertise.posBanner?.getProperty(BannerType.Downloaded), BannerProperty::class.java)}")
            logAdv("BannersLocal : ${GsonParser.toJson(POSAdvertise.posBanner?.getProperty(BannerType.Local), BannerProperty::class.java)}")
            logAdv("ScreenSaver : ${GsonParser.toJson(POSAdvertise.posScreenSaver?.property, ScreenSaverProperty::class.java)}")
            logAdv("tutorial : ${GsonParser.toJson(POSAdvertise.posTutorials?.property, TutorialProperty::class.java)}")
        }
    }

    fun downloadFileFromServer(downloadFileUrl : String, callback: POSAdvertiseCallback.Download<Boolean>) {
        val isUpdateAvailable = POSAdvertisePreference.isUpdateAvailable(this)
        if(isUpdateAvailable) {
            POSAdvertise.downloadFileFromServer(this, downloadFileUrl, callback)
        }
    }

    override fun onDownloadCompletedUpdateUi() {
        updatePOSAdvertiseUiAndProperty()
    }

    private fun updatePOSAdvertiseUiAndProperty() {
        POSAdvertise.updateUICallback()
    }

    private fun initLibs(versionName: String) {
        getPosAdvertise()
            .init(this, versionName, object : POSAdvertiseCallback.Callback<Boolean>{
                override fun onSuccess(response: Boolean) {
                    logAdv("initLocalZipFileExtraction: onSuccess")
                    POSAdvertise.updateUICallback()
                }

                override fun onFailure(e: Exception?) {
                    super.onFailure(e)
                    logAdv("initLocalZipFileExtraction: onFailure")
                }
            })
    }

    fun getPosAdvertise(): POSAdvertise {
        if(posAdvertise == null) {
            posAdvertise = POSAdvertise.apply {
                isDebugMode = isDebugMode()
                posBanner = getPOSBanner()
                posScreenSaver = getPOSScreenSaver()
                posTutorials = getPOSTutorial()
                addListener(this@POSAdvertiseApplication.hashCode(), this@POSAdvertiseApplication)
            }
        }
        return posAdvertise!!
    }

    override fun onTerminate() {
        super.onTerminate()
        getPosAdvertise().removeListener(this@POSAdvertiseApplication.hashCode())
    }

    private fun getPOSBanner(): POSBanner {
        return POSBanner.apply {
            setProperty(this@POSAdvertiseApplication)
        }
    }

    private fun getPOSScreenSaver(): POSScreenSaver {
        return POSScreenSaver.apply {
            property = POSAdvertiseUtility.getSSProperty(this@POSAdvertiseApplication)
            init(this@POSAdvertiseApplication)
        }
    }

    private fun getPOSTutorial(): POSTutorials {
        return POSTutorials.apply {
            property = POSAdvertiseUtility.getTutorialProperty(this@POSAdvertiseApplication)
        }
    }

    fun registerScreenSaver(context: Context) {
//        getPosAdvertise().registerScreenSaver(context)
    }

    fun unregisterPosAdvertise() {
//        getPosAdvertise().unregisterScreenSaver()
    }

}