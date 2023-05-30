package com.posadvertise.tutorials.view

import android.content.Context
import android.graphics.drawable.Animatable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.helper.util.WakeLockHandler
import com.posadvertise.POSAdvertise
import com.posadvertise.databinding.AdvFragmentTutorialViewerBinding
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.POSFileManager
import com.posadvertise.util.common.AdvertiseModel
import com.posadvertise.util.common.POSBaseFragment
import java.io.File


class TutorialGifFragment : POSBaseFragment() {

    private lateinit var fileManager: POSFileManager
    private var wakeLock: WakeLockHandler? = null
    private var mTutorial: AdvertiseModel? = null
    private var viewBinding: AdvFragmentTutorialViewerBinding? = null
    private var mGifDrawable : GifDrawable? = null

    override fun onDestroyView() {
        super.onDestroyView()
        viewBinding = null
        wakeLock?.stopWakeLock()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        POSAdvertise.startScreenSaverFreeze(this)
        wakeLock = WakeLockHandler(requireContext(), TutorialGifFragment::class.java.simpleName)
            .startWakeLock()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = AdvFragmentTutorialViewerBinding.inflate(layoutInflater, container, false)
        return viewBinding?.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mTutorial = mProperty.model
        mTutorial?.let {
            fileManager = POSAdvertiseUtility.getStorage()
            loadGif(viewBinding?.ivGifViewer!!, it)
        }

        viewBinding?.ivGifViewer?.setOnClickListener{
            viewBinding?.ivPlay?.visibility = View.VISIBLE
            if(viewBinding?.ivGifViewer?.drawable is Animatable){
                (viewBinding?.ivGifViewer?.drawable as Animatable).stop()
            }
            wakeLock?.stopWakeLock()
        }

        viewBinding?.ivPlay?.setOnClickListener{
            viewBinding?.ivPlay?.visibility = View.GONE
            if(viewBinding?.ivGifViewer?.drawable is Animatable){
                (viewBinding?.ivGifViewer?.drawable as Animatable).start()
            }
            wakeLock?.startWakeLock()
        }
    }

    override fun onResume() {
        super.onResume()
        if(viewBinding?.ivPlay?.isVisible == true){
            viewBinding?.ivPlay?.visibility = View.GONE
        }
    }


    private fun loadGif(imageView: ImageView, item: AdvertiseModel) {
        try {
            if (!TextUtils.isEmpty(item.fileName)) {
                Glide.with(this)
                    .asGif()
                    .load(getImageUri(imageView.context, item.fileName!!))
                    .addListener(object : RequestListener<GifDrawable>{
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                            return false;
                        }

                        override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            resource?.setLoopCount(1)
                            resource?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
                                override fun onAnimationEnd(drawable: Drawable) {
                                    mGifDrawable = resource
                                    wakeLock?.stopWakeLock()
                                    viewBinding?.ivPlay?.visibility = View.VISIBLE
                                }
                            })
                            return false
                        }
                    })
                    .into(imageView)
            } else {
                item.imageRes?.let {
                    if(it != 0) {
                        Glide.with(this).asGif().load(it).into(imageView)
                    }
                }
            }
        } catch (e: Exception) {
        }
    }

    private fun getImageUri(context: Context, fileName: String): Uri {
        val folder = fileManager.getPOSTutorialFolder(context)
        return fileManager.getUriFromFile(context, File(folder, fileName))
    }

}