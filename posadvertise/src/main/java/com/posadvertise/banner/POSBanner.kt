package com.posadvertise.banner

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.posadvertise.POSAdvertise
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.banner.model.BannerType
import com.posadvertise.banner.model.BannerViewType
import com.posadvertise.banner.views.FragmentBanner
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.ExtraProperty
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun logBanner(message: String?){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSBanner::class.java.simpleName, message ?: "")
    }
}

object POSBanner {

    const val BannerAttachDelayTime : Long = 3 * 1000

    var mProperty: BannerProperty? = null
    private var propertyLocal: BannerProperty? = null
//    get() {
//        if(field == null) field = BannerProperty()
//        return field
//    }

    fun setProperty(context: Context?) {
        this.mProperty = POSAdvertiseUtility.getBannerProperty(context, true)
        this.propertyLocal = POSAdvertiseUtility.getBannerProperty(context, false)
    }

    fun getProperty(bannerType: BannerType) : BannerProperty?{
        if(bannerType == BannerType.Downloaded){
            return mProperty
        }else if(bannerType == BannerType.Local){
            return propertyLocal
        }else{
            return if(mProperty != null) mProperty else propertyLocal
        }
    }

    fun show(activity: Activity, container: Int, property: ExtraProperty) {
        logBanner("isValidDateTimeToShow = true")
        if(isValidBanners(BannerViewType.ALL, property.bannerType)) {
            (activity as AppCompatActivity).lifecycleScope.launch {
                delay(getBannerAttachDelayTime(property.bannerType))
                //Start Task
                activity.supportFragmentManager.beginTransaction().apply {
                    replace(container, FragmentBanner().apply {
                        arguments = POSAdvertiseUtility.getBundle(property)
                    })
                }.commitAllowingStateLoss()
            }
        }else{
            logBanner("isValidBanners : false")
        }
    }

    fun show(fragment: Fragment, container: Int, property: ExtraProperty) {
        if(isValidBanners(property.viewType, property.bannerType)) {
            fragment.lifecycleScope.launch {
                delay(getBannerAttachDelayTime(property.bannerType))
                //Start Task
                mapFragment(fragment, property, container)
                logBanner("isValidBanners : true")
            }
        }else{
            logBanner("isValidBanners : false")
        }
    }

    private fun mapFragment(fragment: Fragment, property: ExtraProperty, container: Int) {
        fragment.childFragmentManager.beginTransaction().apply {
            replace(container, FragmentBanner().apply {
                arguments = POSAdvertiseUtility.getBundle(property)
            })
        }.commitAllowingStateLoss()
    }

    private fun getBannerAttachDelayTime(bannerType: BannerType): Long {
        getProperty(bannerType)?.let {
            return it.getBannerAttachDelayTime()
        }
        return BannerAttachDelayTime
    }

    private fun isValidDate(startDate : String?, endDate : String?): Boolean {
        return (System.currentTimeMillis() > POSAdvertiseUtility.getTimeInMillis(startDate)
                && System.currentTimeMillis() < POSAdvertiseUtility.getTimeInMillis(endDate))
    }

    private fun isValidBanners(type: BannerViewType, bannerType: BannerType): Boolean {
        val mList : List<AdvertiseModel>? = getBannerList(bannerType)
        mList?.let {
            for (item in mList){
                if(isValidType(item, type) && isValidDate(item.startDate, item.endDate)){
                    return true
                }
            }
        }
        return false
    }

    private fun getBannerList(bannerType: BannerType): List<AdvertiseModel>? {
        return getProperty(bannerType)?.list
    }

    private fun isValidType(item: AdvertiseModel, type: BannerViewType): Boolean {
        if(type == BannerViewType.ALL){
            return true
        }
        if(item.showIn?.equals(BannerViewType.ALL.value, ignoreCase = true) == true){
            return true
        }
        return item.showIn?.contains(type.value, ignoreCase = true) == true
    }

    fun getValidBanners(property: ExtraProperty): MutableList<AdvertiseModel> {
        val finalList: MutableList<AdvertiseModel> = mutableListOf()
        val mList = getBannerList(property.bannerType)
        mList?.let {
            for (item in mList){
                if(isValidType(item, property.viewType) && isValidDate(item.startDate, item.endDate)){
                    finalList.add(item)
                }
            }
        }
        return finalList
    }
}
