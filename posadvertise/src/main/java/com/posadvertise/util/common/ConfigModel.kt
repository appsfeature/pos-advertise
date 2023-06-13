package com.posadvertise.util.common

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfigModel(
    var bannerVersion: Int = 0,
) : Parcelable {
}