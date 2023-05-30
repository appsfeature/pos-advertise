package com.posadvertise.banner.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.banner.POSBanner
import com.posadvertise.databinding.AdvFragmentBannerBinding
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.POSAutoSlider
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseType
import com.posadvertise.util.common.AdvertiseViewModel
import com.posadvertise.util.common.POSBaseFragment


class FragmentBanner : POSBaseFragment() {

    private var isActionPerformed: Boolean = false
    private var viewBinding: AdvFragmentBannerBinding? = null
    private lateinit var viewModel: AdvertiseViewModel
    private var autoSlider : POSAutoSlider? = null

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AdvFragmentBannerBinding.inflate(layoutInflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isActionPerformed = mProperty.isActionPerformed
        viewModel = ViewModelProvider(requireActivity())[AdvertiseViewModel::class.java]
        autoSlider = POSAutoSlider(viewBinding?.viewPager, viewBinding?.indicatorView, mProperty, getSliderDelayTime())
        autoSlider?.apply {
            designType = AdvertiseType.Banner
            initUi(lifecycle, object : POSAdvertiseCallback.OnClickListener {
                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
                    if(isActionPerformed) {
                        POSAdvertise.onBannerItemClicked(view?.context, item)
                    }
                }
            })
        }

        viewModel.apply {
            loadBannerData(mProperty)
            liveBannerList.observe(viewLifecycleOwner, Observer {
                autoSlider?.loadViewPager(it)
            })
        }
    }

    private fun getSliderDelayTime(): Long {
        return POSBanner.getProperty(mProperty.bannerType)?.getBannerTransitionTime() ?: POSAdvertiseConstants.DefaultTransitionTime
    }


}