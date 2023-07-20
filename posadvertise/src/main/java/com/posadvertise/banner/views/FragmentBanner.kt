package com.posadvertise.banner.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
import com.posadvertise.util.common.POSBaseFragment
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class FragmentBanner : POSBaseFragment() {

    private var viewPager: ViewPager2? = null
    private var indicatorView: WormDotsIndicator? = null
    private var isActionPerformed: Boolean = false
    private var viewModel: AdvertiseViewModel? = null
    private var autoSlider: POSAutoSlider? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view: View = inflater.inflate(
            if (mProperty.isBannerIndicatorTop) R.layout.adv_fragment_banner_top else R.layout.adv_fragment_banner,
            container,
            false
        )
        initView(view)
        return view
    }

    private fun initView(view: View) {
        viewPager = view.findViewById(R.id.view_pager)
        indicatorView = view.findViewById(R.id.indicator_view)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isActionPerformed = mProperty.isActionPerformed
        viewModel = ViewModelProvider(requireActivity())[AdvertiseViewModel::class.java]
        autoSlider = POSAutoSlider(viewPager, indicatorView, mProperty, getSliderDelayTime())
        autoSlider?.apply {
            designType = AdvertiseType.Banner
            initUi(lifecycle, object : POSAdvertiseCallback.OnClickListener {
                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
                    if (isActionPerformed) {
                        POSAdvertise.onBannerItemClicked(view?.context, item)
                    }
                }
            })
        }

        viewModel?.apply {
            loadBannerData(mProperty)
            liveBannerList.observe(viewLifecycleOwner, Observer {
                autoSlider?.loadViewPager(it)
            })
        }
    }

    private fun getSliderDelayTime(): Long {
        return POSBanner.getProperty(mProperty.bannerType)?.getBannerTransitionTime()
            ?: POSAdvertiseConstants.DefaultTransitionTime
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel = null
        autoSlider = null
        viewPager = null
        indicatorView = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel = null
        autoSlider = null
        viewPager = null
        indicatorView = null
    }

}

