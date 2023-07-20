package com.posadvertise.tutorials.model

import android.os.Parcelable
import com.posadvertise.util.common.AdvertiseModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class TutorialProperty(
    var isEnable: Boolean = false,
    var list: List<AdvertiseModel> = listOf(),
) : Parcelable{

}
