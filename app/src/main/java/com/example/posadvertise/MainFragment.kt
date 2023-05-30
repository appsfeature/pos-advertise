package com.example.posadvertise

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.posadvertise.databinding.FragmentMainBinding
import com.posadvertise.POSAdvertise
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.util.POSAdvertisePreference
import com.posadvertise.util.POSAdvertiseUtility
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showBanner()

        binding.btnAction.setOnClickListener {
            showProgress(View.VISIBLE)
            downloadFiles(AppApplication.fileUrlAll)
        }
        binding.btnUpdate.setOnClickListener {
//            showProgress(View.VISIBLE)
//            downloadFiles(AppApplication.fileUrlPartialUpdate)
            POSAdvertiseUtility.installAPK(requireActivity().applicationContext)
        }
        binding.btnOpen.setOnClickListener {
            POSAdvertise.openTutorial(requireActivity(), "Tutorials")
        }
        binding.btnOpen2.setOnClickListener {
            POSAdvertise.openTutorialById(requireActivity(), "Sale Tutorials", 1)
        }

        binding.tvAppVersion.text = "Version : ${BuildConfig.VERSION_NAME}"
    }

    private fun downloadFiles(fileUrlAll: String) {
        POSAdvertise.downloadFileFromServer(requireActivity(), fileUrlAll, object  : POSAdvertiseCallback.Download<Boolean>{
            override fun onSuccess(response: Boolean) {

            }
            override fun onProgressUpdate(progress: Int) {
                binding.progressHorizontal.progress = progress
                if(progress == 100){
                    showProgress(View.GONE)
                }
            }
        })
    }

    private fun showBanner() {
        AppApplication.instance.getPosAdvertise().showBannerOnHomeScreen(this, R.id.container_banner)
    }

    private fun showProgress(visible: Int) {
        binding.llDownload.visibility = visible
        binding.progressHorizontal.progress = 0
        binding.progressHorizontal.visibility = visible
    }
}