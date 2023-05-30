package com.posadvertise.util.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AdvertiseModel(
    var id: Int = 0,
    var imageRes: Int? = 0,
    var actionType: Int = 0,
    var fileName: String? = "",
    var ranking: Int = 0,
    var title: String? = "",
    var subTitle: String? = null,
    var actionText: String? = null,
    var startDate: String? = null,
    var endDate: String? = null,
    var isDynamic: Boolean = true,
    var showIn: String? = null,
//    var fileType: Int = FileActionType.Replace.value,
) : Parcelable {
    constructor() : this(0)
}