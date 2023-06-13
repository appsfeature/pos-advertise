package com.posadvertise.util.common

import android.os.Parcelable
import com.posadvertise.banner.model.BannerType
import com.posadvertise.banner.model.BannerViewType
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExtraProperty(
    var id: Int = 0,
    var title: String? = null,
    var isActionPerformed: Boolean = false,
    var bannerType: BannerType = BannerType.Both, //show downloaded banner if available else show locally.
    var viewType: BannerViewType = BannerViewType.ALL,
    var model: AdvertiseModel? = null,
    var isBannerIndicatorTop: Boolean = false,
    ) : Parcelable {
    constructor(mModel: AdvertiseModel) : this(model = mModel)
}