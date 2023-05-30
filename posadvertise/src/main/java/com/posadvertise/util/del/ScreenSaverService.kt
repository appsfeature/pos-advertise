//package com.posadvertise.screensaver.view
//
//import android.service.dreams.DreamService
//import android.text.TextUtils
//import android.util.Log
//import android.view.View
//import com.posadvertise.POSAdvertiseCallback
//import com.posadvertise.R
//import com.posadvertise.banner.POSBanner
//import com.posadvertise.util.common.AdvertiseModel
//import com.posadvertise.util.common.AdvertiseViewModel
//import com.posadvertise.util.AutoSlider
//import com.posadvertise.util.common.AdvertiseType
//
//class ScreenSaverService : DreamService() {
//
//    private var presenter : AdvertiseViewModel? = null
//    private var autoSlider : AutoSlider? = null
//
//    override fun onAttachedToWindow() {
//        super.onAttachedToWindow()
//        // Hide system UI
//        isInteractive = true
//        isFullscreen = true
//        isScreenBright = true
//        // Set the dream layout
//        setContentView(R.layout.adv_screen_saver)
//        presenter = AdvertiseViewModel()
//        autoSlider = AutoSlider(findViewById(R.id.view_pager))
//        autoSlider?.apply {
//            designType = AdvertiseType.ScreenSaver
//            initUi(null, object : POSAdvertiseCallback.OnClickListener {
//                override fun onItemClicked(view: View?, item: AdvertiseModel?) {
//                    view?.let {
//                        if(!TextUtils.isEmpty(it.tag.toString())){
//                            POSBanner.callback?.onItemClicked(view.context, item)
//                        }
//                    }
//                    finish()
//                }
//            })
//        }
//        presenter?.loadScreenSaverData(object : POSAdvertiseCallback.Callback<List<AdvertiseModel>>{
//            override fun onSuccess(response: List<AdvertiseModel>) {
//                autoSlider?.loadViewPager(response)
//            }
//        })
//    }
//
//    override fun onDreamingStarted() {
//        super.onDreamingStarted()
//    }
//
//    override fun onDetachedFromWindow() {
//        super.onDetachedFromWindow()
//        autoSlider?.destroySlider()
//    }
//}