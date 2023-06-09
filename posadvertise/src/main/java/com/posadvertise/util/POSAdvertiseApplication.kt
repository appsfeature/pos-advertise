package com.posadvertise.util


import android.app.Application
import android.content.Context
import com.google.gson.Gson
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.POSBanner
import com.posadvertise.banner.model.BannerType
import com.posadvertise.logAdv
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.tutorials.POSTutorials
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class POSAdvertiseApplication : Application(), POSAdvertiseCallback.OnAdvertiseListener {

    private var posAdvertise : POSAdvertise? = null

    abstract fun isDebugMode() : Boolean;

    abstract fun getVersionCode() : Int;

    override fun onCreate() {
        super.onCreate()
//        //call this method for re-extract local zip file.
//        POSAdvertisePreference.setInternalZipFileExtracted(this, false)
        initLibs(getVersionCode())

        if (POSAdvertise.isDebugMode) {
            getTempLocalJson()
        }
    }

    private fun getTempLocalJson() {
        CoroutineScope(Dispatchers.IO).launch {
            logAdv("Banners : ${Gson().toJson(POSAdvertise.posBanner?.getProperty(BannerType.Downloaded))}")
            logAdv("BannersLocal : ${Gson().toJson(POSAdvertise.posBanner?.getProperty(BannerType.Local))}")
            logAdv("ScreenSaver : ${Gson().toJson(POSAdvertise.posScreenSaver?.property)}")
            logAdv("tutorial : ${Gson().toJson(POSAdvertise.posTutorials?.property)}")
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

    private fun initLibs(versionCode: Int) {
        getPosAdvertise()
            .init(this, versionCode, object : POSAdvertiseCallback.Callback<Boolean>{
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