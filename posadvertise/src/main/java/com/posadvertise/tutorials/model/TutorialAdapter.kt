package com.example.gifmodule.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.posadvertise.POSAdvertiseCallback
import com.posadvertise.R
import com.posadvertise.util.common.AdvertiseModel


class TutorialAdapter(private val mList: List<AdvertiseModel>, val callback: POSAdvertiseCallback.OnItemClickListener<AdvertiseModel>) :
    RecyclerView.Adapter<TutorialAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.adv_slot_tutorial, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = mList[position]
        holder.textView.text = "${position+1}. ${item.title}"
        holder.textView.setOnClickListener {
            callback.onItemClicked(holder.imageView, item)
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivTutorialImage)
        val textView: TextView = itemView.findViewById(R.id.tvTutorialName)
    }
}
