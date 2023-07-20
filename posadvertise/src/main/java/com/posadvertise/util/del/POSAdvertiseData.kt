//package com.example.posadvertise
//
//import android.content.Context
//import com.google.gson.Gson
//import com.google.gson.reflect.TypeToken
//import com.posadvertise.banner.logBanner
//import com.posadvertise.banner.model.BannerProperty
//import com.posadvertise.screensaver.logSS
//import com.posadvertise.screensaver.model.ScreenSaverProperty
//import com.posadvertise.tutorials.logTut
//import com.posadvertise.tutorials.model.TutorialProperty
//import com.posadvertise.util.POSAdvertisePreference
//import com.posadvertise.util.common.AdvertiseModel
//
//object POSAdvertiseData {
//
//
//    fun getBannerProperty(context: Context): BannerProperty? {
//        val savedJson = POSAdvertisePreference.getConfigBannerJson(context)
//        if(!savedJson.isNullOrEmpty()){
//            logBanner("configBannerJson : $savedJson")
//            val mProperty : BannerProperty? = Gson().fromJson(savedJson, object : TypeToken<BannerProperty>() {}.type)
//            mProperty?.let {
////                if(it.list.isEmpty()){
////                    it.list = getBannerLocalList()
////                }
//                return it
//            }
//        }
//        return null
////        return BannerProperty().apply {
////            isEnableHomeScreen = true
////            isEnablePinEntryScreen = true
////            isEnableVoidScreen = true
////            startDate = "2023-04-25 12:00:00"
////            endDate = "2023-05-30 12:00:00"
////            setBannerTransitionTimeInSec(5)
////            setBannerAttachDelayTimeSec(3)
////            list = getBannerLocalList()
////        }
//    }
//
//    fun getSSProperty(context: Context): ScreenSaverProperty? {
//        val savedJson = POSAdvertisePreference.getConfigScreenSaverJson(context)
//        if(!savedJson.isNullOrEmpty()){
//            logSS("configSSJson : $savedJson")
//            val mProperty : ScreenSaverProperty? = Gson().fromJson(savedJson, object : TypeToken<ScreenSaverProperty>() {}.type)
//            mProperty?.let {
////                if(it.list.isEmpty()){
////                    it.list = getSSLocalList()
////                }
//                return it
//            }
//        }
//        return null
////        return ScreenSaverProperty().apply {
////            isEnableStaticQRCode = true
////            isEnableShowWhenChargerConnected = true
////            screenTimeOut = 60 * 1000
////            setTransitionTime(5000)
////            screenTimeOut = 13 * 1000
////            startDate = "2023-04-25 12:00:00"
////            endDate = "2023-05-30 12:00:00"
////            list = getSSLocalList()
////        }
//    }
//
//    fun getTutorialProperty(context: Context): TutorialProperty? {
//        val savedJson = POSAdvertisePreference.getConfigTutorialJson(context)
//        if(!savedJson.isNullOrEmpty()){
//            logTut("configTutorialJson : $savedJson")
//            val mProperty : TutorialProperty? = Gson().fromJson(savedJson, object : TypeToken<TutorialProperty>() {}.type)
//            mProperty?.let {
////                if(it.list.isEmpty()){
////                    it.list = getTutorialList()
////                }
//                return it
//            }
//        }
//        return null
////        return TutorialProperty().apply {
////            isEnable = true
////            list = getTutorialList()
////        }
//    }
//
//    private fun getBannerLocalList(): List<AdvertiseModel> {
//        val list = arrayListOf<AdvertiseModel>()
//        list.add(AdvertiseModel(0, R.drawable.banner1, 1001, startDate = "2023-04-25 12:00:00", endDate = "2023-05-25 12:00:00"))
//        list.add(AdvertiseModel(1, R.drawable.banner2, 1002, startDate = "2023-04-25 12:00:00", endDate = "2023-05-25 12:00:00"))
////        list.add(AdvertiseModel(2, R.drawable.banner3, 1003, startDate = "2023-04-25 12:00:00", endDate = "2023-05-25 12:00:00"))
//        return list
//    }
//
//    private fun getSSLocalList(): List<AdvertiseModel> {
//        val list = arrayListOf<AdvertiseModel>()
//        list.add(AdvertiseModel(0, R.drawable.ss1, 0, startDate = "2023-04-25 12:00:00", endDate = "2023-05-25 12:00:00"))
//        list.add(AdvertiseModel(1, R.drawable.ss2, 2002, actionText = "Click here to Proceed", startDate = "2023-04-25 12:00:00", endDate = "2023-05-25 12:00:00"))
////        list.add(AdvertiseModel(2, R.drawable.ss3, 2003, startDate = "2023-04-25 12:00:00", endDate = "2023-05-25 12:00:00"))
//        return list
//    }
//
//    private fun getTutorialList(): List<AdvertiseModel> {
//        val list = arrayListOf<AdvertiseModel>()
//        list.add(AdvertiseModel(0, title = "Tutorial 1", imageRes = R.drawable.tutorial_brand_emi,  actionType = 1001, ranking = 1))
//        list.add(AdvertiseModel(1, title = "Tutorial 2", imageRes = R.drawable.tutorial_brand_emi, actionType = 1002, ranking = 2))
//        return list
//    }
//}