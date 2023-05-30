package com.posadvertise.util.common

import androidx.annotation.IntDef




//@IntDef(AdvertiseType.Banner, AdvertiseType.ScreenSaver, AdvertiseType.Tutorials)
//@Retention(AnnotationRetention.SOURCE)
//annotation class AdvertiseType{
//    companion object{
//        const val Banner = 0
//        const val ScreenSaver = 1
//        const val Tutorials = 2
//    }
//}

enum class FileActionType(val value : Int){
    Replace(0),
    Insert(1),
    Ignore(2)
}