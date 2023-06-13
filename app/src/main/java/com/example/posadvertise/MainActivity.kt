package com.example.posadvertise

import android.content.Context
import android.os.Bundle
import android.util.Log
import com.example.posadvertise.databinding.ActivityMainBinding
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.R
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.BasePOSAdvertiseActivity


class MainActivity : BasePOSAdvertiseActivity() , POSAdvertiseCallback.OnAdvertiseListener{
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        attachFragment()
    }

    private fun attachFragment() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content, MainFragment())
        }.commitAllowingStateLoss()
    }

    override fun onStop() {
        super.onStop()
        Log.d("@Test", "onStop")
        POSAdvertise.removeListener(this@MainActivity.hashCode())
    }

    override fun onStart() {
        super.onStart()
        Log.d("@Test", "onStart")
        POSAdvertise.addListener(this@MainActivity.hashCode(), this)
    }

    override fun onBannerItemClicked(context: Context?, item: AdvertiseModel?) {
    }

    override fun onScreenSaverItemClicked(context: Context?, item: AdvertiseModel?) {
    }

    override fun onDownloadCompletedUpdateUi() {
    }
}