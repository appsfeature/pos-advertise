package com.posadvertise.util.common

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.POSBanner
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.tutorials.POSTutorials
import com.posadvertise.tutorials.logTut
import com.posadvertise.util.POSAdvertiseDataManager

class AdvertiseViewModel : ViewModel() {

    var liveBannerList = MutableLiveData<List<AdvertiseModel>>()


    fun loadBannerData(property: ExtraProperty) {
        loadFromLocalData(sortArray(POSBanner.getValidBanners(property)))
    }

    private fun sortArray(mList: List<AdvertiseModel>): List<AdvertiseModel> {
        return mList.sortedWith(compareBy { it.ranking })
    }

    private fun loadFromLocalData(localList: List<AdvertiseModel>) {
        liveBannerList.value = localList
    }

    fun loadScreenSaverData(callback: POSAdvertiseCallback.Callback<List<AdvertiseModel>>) {
        loadSSFromLocalData(POSScreenSaver.getValidScreenSavers(), callback)
    }

    private fun loadSSFromLocalData(localList: List<AdvertiseModel>, callback: POSAdvertiseCallback.Callback<List<AdvertiseModel>>) {
        if(localList.isNotEmpty()) {
            callback.onSuccess(sortArray(localList))
        }else{
            callback.onFailure(Exception("No Data."))
        }
    }

    fun loadTutorialData(context: Context, callback: POSAdvertiseCallback.Callback<List<AdvertiseModel>>) {
        POSTutorials.property?.let {
            loadTutorialFromLocalData(context, it.list, callback)
        }
    }

    private fun loadTutorialFromLocalData(context: Context, localList: List<AdvertiseModel>, callback: POSAdvertiseCallback.Callback<List<AdvertiseModel>>) {
        if(localList.isNotEmpty()) {
            callback.onSuccess(sortArray(localList))
        }else{
            loadTutorialFromServer(context, callback)
        }
    }

    private fun loadTutorialFromServer(context: Context, callback: POSAdvertiseCallback.Callback<List<AdvertiseModel>>) {
        POSAdvertise.httpsTutorialUrl?.let {
            POSAdvertiseDataManager.downloadUpdate(
                context, it,
                object : POSAdvertiseCallback.Download<Boolean> {
                    override fun onSuccess(response: Boolean) {
                        logTut("syncPOSAdvertise: onSuccess")
                        POSTutorials.property?.let {
                            callback.onSuccess(sortArray(it.list))
                        }
                    }

                    override fun onProgressUpdate(progress: Int) {

                    }

                    override fun onFailure(e: Exception?) {
                        super.onFailure(e)
                        callback.onFailure(e)
                        logTut("syncPOSAdvertise: onFailure")
                    }
                })
        }
    }
}