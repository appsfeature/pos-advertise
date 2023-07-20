package com.posadvertise.util.common

import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.R
import com.posadvertise.screensaver.logSS
import com.posadvertise.util.POSAdvertiseUtility
import com.posadvertise.util.POSFileManager
import java.io.File


class AutoSliderAdapter(
    private val mItemType: AdvertiseType,
    val mList: List<AdvertiseModel>,
    private val mProperty: ExtraProperty,
    listener: POSAdvertiseCallback.OnClickListener?
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var clickListener: POSAdvertiseCallback.OnClickListener?
    private var fileManager: POSFileManager

    init {
        this.clickListener = listener
        this.fileManager = POSAdvertiseUtility.getStorage()
    }
    
    override fun getItemViewType(position: Int): Int {
        return mItemType.value
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == AdvertiseType.Banner.value) {
            BannerHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.adv_slot_banner, parent, false)
            )
        } else {
            SSViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.adv_slot_screen_saver, parent, false)
            )
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, i: Int) {
        val item = mList[i]
        if (viewHolder is BannerHolder) {
            loadImage(viewHolder.ivImage, item)
        } else if (viewHolder is SSViewHolder) {
            loadImage(viewHolder.ivImage, item)
            if (item.actionType > 0) {
                viewHolder.btnAction.visibility = View.VISIBLE
                viewHolder.btnAction.text =
                    if (TextUtils.isEmpty(item.actionText)) "Proceed" else item.actionText
            } else {
                viewHolder.btnAction.visibility = View.GONE
            }
        }
    }

    private fun loadImage(imageView: ImageView, item: AdvertiseModel) {
        try {
            if (!TextUtils.isEmpty(item.fileName)) {
                val imageUri = getImageUri(imageView.context, item.fileName!!)
                if(item.fileName!!.endsWith(".gif")){
                    Glide.with(imageView.context)
                        .asGif()
                        .load(imageUri)
                        .into(imageView)
                }else{
                    imageView.setImageURI(imageUri)
                }
            } else {
                item.imageRes?.let {
                    imageView.setImageResource(it)
                }
            }
        } catch (e: Exception) {
            logSS(e.toString())
            logSS("Need to add <files-path name=\"files\" path=\".\" /> in provider_paths.xml file.")
        }
    }

    private fun getImageUri(context: Context, fileName: String): Uri {
        val folder = getFolder(context)
        return getUriFromFile(context, File(folder, fileName))
    }

    private var fileBanner: File? = null
    private var fileSS: File? = null

    private fun getFolder(context: Context): File {
        if (mItemType == AdvertiseType.Banner) {
            if (fileBanner == null) {
                fileBanner = fileManager.getPOSBannerFolder(context, mProperty.bannerType)
            }
            return fileBanner!!
        } else {
            if (fileSS == null) {
                fileSS = fileManager.getPOSScreenSaverFolder(context)
            }
            return fileSS!!
        }
    }

    fun getUriFromFile(context: Context, file: File): Uri {
        val fileProvider =
            context.packageName + context.getString(R.string.file_provider)
        return FileProvider.getUriForFile(context, fileProvider, file)
    }


    inner class BannerHolder(v: View) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        val ivImage: ImageView

        init {
            ivImage = v.findViewById(R.id.iv_banner)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (adapterPosition >= 0 && adapterPosition < mList.size) {
                clickListener?.onItemClicked(v, mList.get(adapterPosition))
            }
        }
    }

    inner class SSViewHolder(v: View) : RecyclerView.ViewHolder(v),
        View.OnClickListener {
        val ivImage: ImageView
        val btnAction: Button

        init {
            ivImage = v.findViewById(R.id.iv_banner)
            btnAction = v.findViewById(R.id.btn_action)
            itemView.setOnClickListener(this)
            btnAction.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            if (v.id == R.id.btn_action) {
                v.tag = "PerformAction"
            } else {
                v.tag = ""
            }
            if (adapterPosition >= 0 && adapterPosition < mList.size) {
                clickListener?.onItemClicked(v, mList.get(adapterPosition))
            }
        }
    }
}