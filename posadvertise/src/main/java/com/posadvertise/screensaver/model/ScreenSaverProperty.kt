package com.posadvertise.screensaver.model

import android.os.Parcelable
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.common.AdvertiseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ScreenSaverProperty(
    var isEnableScreenSaver: Boolean = true,
    var isEnableStaticQRCode: Boolean = false,
    var isEnableShowWhenChargerConnected: Boolean = true,
    var screenTimeOut: Long = 3 * 60 * 1000,
    private var transitionTime: Long = 5 * 1000,
    var list: List<AdvertiseModel> = listOf(),

    ) : Parcelable {
    fun getTransitionTime(): Long = if (transitionTime < 3) POSAdvertiseConstants.DefaultTransitionTime else transitionTime
    fun setTransitionTime(transitionTime : Long){
        this.transitionTime = transitionTime
    }
}