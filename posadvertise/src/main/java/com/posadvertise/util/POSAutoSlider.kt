package com.posadvertise.util

import android.view.View
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.widget.ViewPager2
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseType
import com.posadvertise.util.common.AutoSliderAdapter
import com.posadvertise.util.common.ExtraProperty
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import kotlinx.coroutines.*

class POSAutoSlider(private val viewPager: ViewPager2?, private val indicatorView: WormDotsIndicator?, private val mProperty : ExtraProperty, private val mTransitionTime : Long) : DefaultLifecycleObserver{

    private var mList = mutableListOf<AdvertiseModel>()

    var designType = AdvertiseType.Banner

    fun initUi(lifecycle: Lifecycle?, callback: POSAdvertiseCallback.OnClickListener) {
        lifecycle?.addObserver(this)
        viewPager?.let {
            it.adapter = AutoSliderAdapter(
                designType,
                mList, mProperty, callback
            )
            it.registerOnPageChangeCallback(viewPagerCallback)
            indicatorView?.setViewPager2(it)
        }
    }

    fun loadViewPager(list: List<AdvertiseModel>?) {
        if(!list.isNullOrEmpty()){
            mList.clear()
            mList.addAll(list)
            viewPager?.let {
                it.offscreenPageLimit = mList.size
                it.adapter?.notifyDataSetChanged()
                startSlider()
                it.visibility = View.VISIBLE
                indicatorView?.visibility = if (mList.size > 1) View.VISIBLE else View.GONE
            }
        }else{
            indicatorView?.visibility = View.GONE
            viewPager?.visibility = View.GONE
        }
    }

    var viewPagerCallback: ViewPager2.OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            startSlider()
        }
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        destroySlider()
    }

    private var taskJob: Job? = null

    private fun startSlider() {
        if(isValidViewPager()){
            stopSlider()
            taskJob = CoroutineScope(Dispatchers.Main).launch {
                delay(mTransitionTime)
                // task to perform
                viewPager?.let {
                    val nextPos: Int = it.currentItem + 1
                    if (it.adapter != null && it.adapter!!.itemCount > 0) {
                        if (it.adapter!!.itemCount > nextPos) {
                            it.currentItem = nextPos
                        } else {
                            it.currentItem = 0
                        }
                    }
                }
            }
        }
    }

    private fun isValidViewPager(): Boolean {
        viewPager?.let {
            if (it.adapter != null && it.adapter!!.itemCount > 1) {
                return true
            }
        }
        return false
    }

    private fun stopSlider() {
        taskJob?.cancel()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        startSlider()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        stopSlider()
    }

    fun destroySlider() {
        stopSlider()
        viewPager?.unregisterOnPageChangeCallback(viewPagerCallback)
    }
}