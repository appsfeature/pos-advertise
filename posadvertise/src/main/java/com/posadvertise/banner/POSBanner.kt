package com.posadvertise.banner

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import com.posadvertise.POSAdvertise
import com.posadvertise.banner.model.BannerProperty
import com.posadvertise.banner.model.BannerType
import com.posadvertise.banner.model.BannerViewType
import com.posadvertise.banner.views.BannerViewer
import com.posadvertise.banner.views.FragmentBanner
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.ExtraProperty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun logBanner(message: String?){
    if(POSAdvertise.isDebugMode) {
        Log.d(POSBanner::class.java.simpleName, message ?: "")
    }
}

object POSBanner {

    const val BannerAttachDelayTime : Long = 0

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

//    fun show(activity: Activity, container: Int, property: ExtraProperty) {
//        logBanner("isValidDateTimeToShow = true")
//        if(isValidBanners(BannerViewType.ALL, property.bannerType)) {
//            (activity as AppCompatActivity).lifecycleScope.launch {
//                delay(getBannerAttachDelayTime(property.bannerType))
//                //Start Task
//                activity.supportFragmentManager.beginTransaction().apply {
//                    replace(container, FragmentBanner().apply {
//                        arguments = POSAdvertiseUtility.getBundle(property)
//                    })
//                }.commitAllowingStateLoss()
//            }
//        }else{
//            logBanner("isValidBanners : false")
//        }
//    }

    fun show(lifecycle: Lifecycle?, container: FrameLayout?, property: ExtraProperty) {
        if(isValidBanners(property.viewType, property.bannerType)) {
//            val mView = fragment.view?.findViewById<FrameLayout>(container)
//            CoroutineScope(Dispatchers.Main).launch {
//                mView?.removeAllViews()
//            }
            CoroutineScope(Dispatchers.Main).launch {
                mapFragment(lifecycle, property, container)
                logBanner("isValidBanners : true")
            }
        }else{
            logBanner("isValidBanners : false")
        }
    }
//    fun show(fragment: Fragment, container: Int, property: ExtraProperty) {
//        if(isValidBanners(property.viewType, property.bannerType)) {
////            val mView = fragment.view?.findViewById<FrameLayout>(container)
////            CoroutineScope(Dispatchers.Main).launch {
////                mView?.removeAllViews()
////            }
//            fragment.lifecycleScope.launch {
////                delay(getBannerAttachDelayTime(property.bannerType))
//                //Start Task
//                mapFragment(fragment, property, container)
//                logBanner("isValidBanners : true")
//            }
//        }else{
//            logBanner("isValidBanners : false")
//        }
//    }

    private fun mapFragment(lifecycle: Lifecycle?, property: ExtraProperty, container: FrameLayout?) {
        container?.let {
            BannerViewer.show(property, lifecycle, it)
        }
    }

//    private fun mapFragment(fragment: Fragment, property: ExtraProperty, container: Int) {
//        val fragmentBanner = fragment.childFragmentManager.findFragmentByTag(property.viewType.value)
//        fragmentBanner?.let {
//            fragment.childFragmentManager.beginTransaction().remove(it)
//        }
//        fragment.childFragmentManager.beginTransaction().apply {
//            replace(container, FragmentBanner().apply {
//                arguments = POSAdvertiseUtility.getBundle(property)
//            }, property.viewType.value)
//        }.commitAllowingStateLoss()
//    }

    private fun getBannerAttachDelayTime(bannerType: BannerType): Long {
        getProperty(bannerType)?.let {
            return it.bannerAttachDelayTime
        }
        return BannerAttachDelayTime
    }

    private fun isValidDate(startDate : String?, endDate : String?): Boolean {
        return (System.currentTimeMillis() > POSAdvertiseUtility.getTimeInMillis(startDate)
                && System.currentTimeMillis() < POSAdvertiseUtility.getTimeInMillis(endDate))
    }

    fun isValidBannersForHome(): Boolean {
        return isValidBanners(BannerViewType.HOME, BannerType.Both)
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
