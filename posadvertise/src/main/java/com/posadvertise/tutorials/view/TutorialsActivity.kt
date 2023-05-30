package com.posadvertise.tutorials.view

import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.helper.util.BaseConstants
import com.posadvertise.R
import com.posadvertise.databinding.AdvActivityTutorialBinding
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.ExtraProperty
import com.posadvertise.util.common.POSBaseActivity

class TutorialsActivity : POSBaseActivity() {

    private var mTitle: String? = null
    private lateinit var binding: AdvActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AdvActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mTitle = mProperty.title
        val mVideo = mProperty.model
        setUpToolBar()
        if(mVideo != null) {
            openGifViewer(mVideo)
        }else {
            setFragment(TutorialListFragment().apply {
                arguments = getDefaultBundle()
            })
        }
    }

    private fun openGifViewer(item : AdvertiseModel) {
        val mFrag = TutorialGifFragment().apply {
            val property = mProperty.copy()
            property.model = item
            arguments = POSAdvertiseUtility.getBundle(property)
        }
        setFragment(mFrag)
    }

    private fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.content, fragment)
        }.commitAllowingStateLoss()
    }

    private fun setUpToolBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            if (!TextUtils.isEmpty(mTitle)) {
                title = mTitle
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        updateTitle(mTitle)
        super.onBackPressed()
    }

    fun updateTitle(mTitle : String?) {
        supportActionBar?.apply {
            if (!TextUtils.isEmpty(mTitle)) {
                title = mTitle
            }
        }
    }
}