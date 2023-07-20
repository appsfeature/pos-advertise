package com.posadvertise.tutorials.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gifmodule.adapter.TutorialAdapter
import com.helper.util.BaseConstants
import com.helper.util.BaseUtil
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.R
import com.posadvertise.databinding.AdvFragmentTutorialListBinding
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.AdvertiseViewModel
import com.posadvertise.util.common.ExtraProperty
import com.posadvertise.util.common.POSBaseFragment


class TutorialListFragment : POSBaseFragment() {

    private lateinit var viewModel: AdvertiseViewModel
    private var viewBinding: AdvFragmentTutorialListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AdvFragmentTutorialListBinding.inflate(layoutInflater, container, false)
        return viewBinding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity())[AdvertiseViewModel::class.java]

        BaseUtil.showNoDataProgress(viewBinding?.noData?.llNoData)
        context?.let {
            viewModel.loadTutorialData(it, object : POSAdvertiseCallback.Callback<List<AdvertiseModel>>{
                override fun onSuccess(response: List<AdvertiseModel>) {
                    setTutorialListData(response)
                }

                override fun onFailure(e: Exception?) {
                    setTutorialListData(null)
                }
            })
        }
    }

    private fun setTutorialListData(response: List<AdvertiseModel>?) {
        if(!response.isNullOrEmpty()) {
            viewBinding?.recyclerview?.layoutManager = LinearLayoutManager(requireContext())
            val adapter = TutorialAdapter(
                response,
                object : POSAdvertiseCallback.OnItemClickListener<AdvertiseModel> {
                    override fun onItemClicked(view: View?, item: AdvertiseModel) {
                        val mFrag = TutorialGifFragment().apply {
                            val property = mProperty.copy()
                            property.model = item
                            arguments = POSAdvertiseUtility.getBundle(property)
                        }
                        addFragment(mFrag, item.title)
                    }
                })
            viewBinding?.recyclerview?.adapter = adapter
            BaseUtil.showNoData(viewBinding?.noData?.llNoData, View.GONE)
        }else{
            BaseUtil.showNoData(viewBinding?.noData?.llNoData, View.VISIBLE)
        }
    }

    private fun addFragment(fragment: Fragment, title: String?) {
        if (activity is TutorialsActivity) {
            (activity as TutorialsActivity).updateTitle(title ?: getString(R.string.app_name))
        }
        activity?.supportFragmentManager?.beginTransaction()?.apply {
            add(android.R.id.content, fragment)
            addToBackStack(fragment.javaClass.simpleName)
        }?.commitAllowingStateLoss()
    }
}