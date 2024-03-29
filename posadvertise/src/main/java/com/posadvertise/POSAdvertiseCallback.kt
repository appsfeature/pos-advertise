package com.posadvertise

import android.content.Context
import android.view.View
import com.posadvertise.util.common.AdvertiseModel

interface POSAdvertiseCallback{

    interface Callback<T> {
        fun onSuccess(response: T)
        fun onFailure(e: Exception?) {}
    }

    interface Status<T> {
        fun onSuccess(response: T)
    }


    interface OnAdvertiseUpdate{
        fun onUIUpdate()
    }

    interface OnItemClickListener<T> {
        fun onItemClicked(view: View?, item: T)
    }

    interface TimeOutCallback {
        fun onInActivityFound()
    }

    interface OnClickListener {
        fun onItemClicked(view: View?, item: AdvertiseModel?)
    }
    interface OnAdvertiseActionListener {
        fun onBannerItemClicked(context: Context?, item: AdvertiseModel?)
        fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?)
    }
    interface OnAdvertiseListener {
        fun onBannerItemClicked(context: Context?, item: AdvertiseModel?)
        fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?)
        fun onDownloadCompletedUpdateUi()
    }
    interface Download<T> : Callback<T>{
        fun onProgressStart(){}
        fun onProgressEnd(){}
        fun onProgressUpdate(progress : Int){}
    }
    interface ScreenSaver{
        fun onScreenSaverDismiss()
    }
    interface ScreenSaverForceDismissListener{
        fun onScreenSaverDismissCheck(callback : ScreenSaver)
    }
}