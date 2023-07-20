package com.posadvertise.screensaver.view

import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.util.POSAdvertiseConstants
import com.posadvertise.util.POSAutoSlider
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseType
import com.posadvertise.util.common.AdvertiseViewModel
import com.posadvertise.util.common.ExtraProperty
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


object ScreenSaverViewer {

    fun show(activity : AppCompatActivity?, mProperty: ExtraProperty, viewPager: ViewPager2?, indicatorView: WormDotsIndicator?){
        val autoSlider = POSAutoSlider(viewPager, indicatorView, mProperty, getSliderDelayTime())
        autoSlider.apply {
            designType = AdvertiseType.ScreenSaver
            initUi(activity?.lifecycle, object : POSAdvertiseCallback.OnClickListener {
                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
                    view?.let {
                        if(!TextUtils.isEmpty(it.tag.toString())){
                            POSAdvertise.onScreenSaverItemClicked(view.context, item)
                        }
                    }
                    activity?.finish()
                }
            })
        }

        AdvertiseViewModel().loadScreenSaverData(object : POSAdvertiseCallback.Callback<List<AdvertiseModel>>{
            override fun onSuccess(response: List<AdvertiseModel>) {
                autoSlider.loadViewPager(response)
            }

            override fun onFailure(e: Exception?) {
                super.onFailure(e)
                viewPager?.visibility = View.GONE
                indicatorView?.visibility = View.GONE
            }
        })
    }

    private fun getSliderDelayTime(): Long {
        return POSScreenSaver.property?.getTransitionTime() ?: POSAdvertiseConstants.DefaultTransitionTime
    }

}