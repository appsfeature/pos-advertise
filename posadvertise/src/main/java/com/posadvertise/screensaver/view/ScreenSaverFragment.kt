package com.posadvertise.screensaver.view

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.databinding.AdvFragmentScreenSaverBinding
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.POSAutoSlider
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseType
import com.posadvertise.util.common.AdvertiseViewModel
import com.posadvertise.util.common.POSBaseFragment


class ScreenSaverFragment : POSBaseFragment() {

    private var binding: AdvFragmentScreenSaverBinding? = null
    private var presenter : AdvertiseViewModel? = null
    private var autoSlider : POSAutoSlider? = null

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = AdvFragmentScreenSaverBinding.inflate(layoutInflater, container, false)
        presenter = ViewModelProvider(this)[AdvertiseViewModel::class.java]
        autoSlider = POSAutoSlider(binding?.viewPager, binding?.indicatorView, mProperty, getSliderDelayTime())
        autoSlider?.apply {
            designType = AdvertiseType.ScreenSaver
            initUi(lifecycle, object : POSAdvertiseCallback.OnClickListener {
                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
                    view?.let {
                        if(!TextUtils.isEmpty(it.tag.toString())){
                            POSAdvertise.mListener?.onScreenSaverItemClicked(view.context, item)
                        }
                    }
                    activity?.finish()
                }
            })
        }
        loadScreenSaverData()
        return binding?.root
    }

    private fun getSliderDelayTime(): Long {
        return POSScreenSaver.property?.getTransitionTime() ?: POSAdvertiseConstants.DefaultTransitionTime
    }

    private fun loadScreenSaverData() {
        presenter?.loadScreenSaverData(object : POSAdvertiseCallback.Callback<List<AdvertiseModel>>{
            override fun onSuccess(response: List<AdvertiseModel>) {
                autoSlider?.loadViewPager(response)
            }

            override fun onFailure(e: Exception?) {
                super.onFailure(e)
                binding?.viewPager?.visibility = View.GONE
                binding?.indicatorView?.visibility = View.GONE
            }
        })
    }
}