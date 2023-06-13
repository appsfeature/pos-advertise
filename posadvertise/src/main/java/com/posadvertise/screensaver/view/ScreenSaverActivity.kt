package com.posadvertise.screensaver.view

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.helper.util.WakeLockHandler
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.R
import com.posadvertise.databinding.AdvActivityScreenSaverBinding
import com.posadvertise.screensaver.POSScreenSaver
import com.posadvertise.screensaver.logSS
import com.posadvertise.screensaver.util.ScreenSaverUtil
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.POSBaseActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class ScreenSaverActivity : POSBaseActivity() {

    private var task: Job? = null

    private lateinit var binding: AdvActivityScreenSaverBinding
    private var wakeLock: WakeLockHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdvActivityScreenSaverBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachFragment()
        wakeLock = WakeLockHandler(this, ScreenSaverActivity::class.java.simpleName)
        wakeLock?.startWakeLock()

        POSAdvertise.registerUIUpdateCallbacks(this@ScreenSaverActivity.hashCode(), object :POSAdvertiseCallback.OnAdvertiseUpdate{
            override fun onUIUpdate() {
                attachFragment()
            }
        })

        POSAdvertiseUtility.hideKeyboard(this)
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content, ScreenSaverFragment().apply {
                arguments = getDefaultBundle()
            })
        }.commitAllowingStateLoss()
    }

    override fun onResume() {
        super.onResume()
        checkChargerConnectivity(this)
    }

    private fun checkChargerConnectivity(context: Context) {
        task?.cancel()
        task = lifecycleScope.launch {
            delay(getScreenTimeOut())
            if(ScreenSaverUtil.isChargerConnected(context)){
                logSS("checkChargerConnectivity isConnected : true")
                wakeLock?.resumeWakeLock()
                checkChargerConnectivity(context)
            }else{
                logSS("checkChargerConnectivity isConnected : false")
                wakeLock?.stopWakeLock()
            }
        }
    }

    private fun getScreenTimeOut(): Long {
        return POSScreenSaver.getConfigScreenTimeout(this)
    }

    override fun onDestroy() {
        POSScreenSaver.resetScreenTimeout(applicationContext)
        logSS("ScreenSaver Stopped")
//        autoSlider?.destroySlider()
        POSAdvertise.unregisterUIUpdateCallbacks(this@ScreenSaverActivity.hashCode())
        task?.cancel()
        wakeLock?.stopWakeLock()
        super.onDestroy()
    }
}