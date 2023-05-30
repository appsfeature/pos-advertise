package com.posadvertise.util.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.helper.util.BaseConstants

open class POSBaseFragment : Fragment() {

    protected lateinit var mProperty: ExtraProperty

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mProperty = arguments?.getParcelable(BaseConstants.EXTRA_PROPERTY) as ExtraProperty? ?: ExtraProperty()
    }

    open fun getBundle(): Bundle {
        val bundle = Bundle()
        bundle.putParcelable(BaseConstants.EXTRA_PROPERTY, mProperty.copy())
        return bundle
    }
}