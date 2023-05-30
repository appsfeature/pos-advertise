package com.example.posadvertise

import android.os.Bundle
import com.example.posadvertise.databinding.ActivityMainBinding
import com.posadvertise.R
import com.posadvertise.util.common.BasePOSAdvertiseActivity


class MainActivity : BasePOSAdvertiseActivity() {
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
}