package com.posadvertise.banner.views

import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.widget.ViewPager2
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.R
import com.posadvertise.banner.POSBanner
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.POSAutoSlider
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseType
import com.posadvertise.util.common.AdvertiseViewModel
import com.posadvertise.util.common.ExtraProperty
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


object BannerViewer {

    private val viewGroup = HashMap<String, FrameLayout>()


    fun show(mProperty: ExtraProperty, lifecycle: Lifecycle?, container: FrameLayout) {
        cleanParentLayoutList()
        val inflater = LayoutInflater.from(container.context)
        container.removeAllViews()
        onViewCreated(inflater.inflate(if (mProperty.isBannerIndicatorTop) R.layout.adv_fragment_banner_top else R.layout.adv_fragment_banner, container)
            , mProperty, lifecycle)
        viewGroup.put(mProperty.viewType.value, container)
    }



    fun onViewCreated(view : View, mProperty: ExtraProperty, lifecycle : Lifecycle?) {
        val viewPager = view.findViewById<ViewPager2>(R.id.view_pager)
        val indicatorView = view.findViewById<WormDotsIndicator>(R.id.indicator_view)

        val isActionPerformed = mProperty.isActionPerformed
        val autoSlider = POSAutoSlider(viewPager, indicatorView, mProperty, getSliderDelayTime(mProperty))
        autoSlider.apply {
            designType = AdvertiseType.Banner
            initUi(lifecycle, object : POSAdvertiseCallback.OnClickListener {
                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
                    if (isActionPerformed) {
                        POSAdvertise.onBannerItemClicked(view?.context, item)
                    }
                }
            })
        }
        AdvertiseViewModel().apply {
            loadBannerData(mProperty, object : POSAdvertiseCallback.Status<List<AdvertiseModel>>{
                override fun onSuccess(response: List<AdvertiseModel>) {
                    autoSlider.loadViewPager(response)
                }
            })
        }
    }

    private fun getSliderDelayTime(mProperty: ExtraProperty): Long {
        return POSBanner.getProperty(mProperty.bannerType)?.getBannerTransitionTime()
            ?: POSAdvertiseConstants.DefaultTransitionTime
    }


    private fun cleanParentLayoutList() {
        for ((k, v) in viewGroup) {
            if (v != null && v.childCount > 0) {
                v.removeAllViews()
            }
        }
        viewGroup.clear()
    }
}

