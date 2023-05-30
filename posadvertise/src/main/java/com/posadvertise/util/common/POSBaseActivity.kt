package com.posadvertise.util.common

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.helper.util.BaseConstants
import com.posadvertise.util.POSAdvertiseConstants

open class POSBaseActivity : AppCompatActivity() {

    protected lateinit var mProperty: ExtraProperty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProperty = intent.getParcelableExtra(BaseConstants.EXTRA_PROPERTY) as ExtraProperty? ?: ExtraProperty()
    }


    open fun getDefaultBundle(): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(BaseConstants.EXTRA_PROPERTY, mProperty.copy())
        return bundle
    }
}