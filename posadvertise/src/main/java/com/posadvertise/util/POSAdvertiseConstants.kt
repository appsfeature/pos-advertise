package com.posadvertise.util

import com.helper.util.BaseConstants

interface POSAdvertiseConstants : BaseConstants{

    companion object{
        const val AdvertiseFileName: String = "POSAdvertise.zip"
        const val AdvertiseFolder: String = "POSAdvertise"

        const val zipFolderBanner: String = "Banner"
        const val zipFolderBannerLocal: String = "Banner-local"
        const val zipFolderScreenSaver: String = "ScreenSaver"
        const val zipFolderTutorial: String = "Tutorial"
        const val zipFolderUpdate: String = "Update"

        const val x : Long = 5 * 1000
        const val DefaultScreenTimeOut: Long = 60 * 1000
        const val DefaultTransitionTime : Long = 5 * 1000

    }
}


