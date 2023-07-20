package com.posadvertise.banner.model


enum class BannerType(val value : String){
    Local("Local"),
    Downloaded("Downloaded"),
    Both("Both"), // show downloaded banners if available else load local.
}