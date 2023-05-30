package com.posadvertise.banner.model

import android.os.Parcelable
import com.posadvertise.banner.POSBanner
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.common.AdvertiseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class BannerProperty(
    private var bannerTransitionTimeInSec: Long = POSAdvertiseConstants.DefaultTransitionTime,
    private var bannerAttachDelayTime: Long = POSBanner.BannerAttachDelayTime,
    var list: List<AdvertiseModel> = listOf(),
) : Parcelable{

    fun getBannerTransitionTime(): Long = if (bannerTransitionTimeInSec < 3) POSAdvertiseConstants.DefaultTransitionTime else bannerTransitionTimeInSec
    fun setBannerTransitionTimeInSec(timeBannerTransition : Long){
        this.bannerTransitionTimeInSec = timeBannerTransition * 1000
    }

    fun getBannerAttachDelayTime(): Long = if (bannerAttachDelayTime < 0) 0 else bannerAttachDelayTime
    fun setBannerAttachDelayTimeSec(bannerAttachDelayTime : Long){
        this.bannerAttachDelayTime = bannerAttachDelayTime * 1000
    }
}
