package com.hurryyu.tworecyclerviewlinkage.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hurryyu.tworecyclerviewlinkage.bean.ClassifyViewBean
import com.hurryyu.tworecyclerviewlinkage.databinding.ItemClassifyBinding

class ClassifyAdapter(private val block: ((position: Int) -> Unit)? = null) :
    RecyclerView.Adapter<ClassifyAdapter.ViewHolder>() {

    val dataList: MutableList<ClassifyViewBean> = mutableListOf()

    var currentSelectedPosition = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var isPrimaryClicked = false

    class ViewHolder(val binding: ItemClassifyBinding, block: ((position: Int) -> Unit)? = null) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                block?.invoke(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemClassifyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            block
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position == currentSelectedPosition) {
            holder.itemView.setBackgroundColor(Color.parseColor("#FFFFFF"))
        } else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT)
        }
        if (dataList[position].productImgUrl.isNotEmpty()) {
            holder.binding.ivClassify.visibility = View.VISIBLE
            holder.binding.ivClassify.load(dataList[position].productImgUrl)
        } else {
            holder.binding.ivClassify.visibility = View.GONE
        }
        holder.binding.tvClassify.text = dataList[position].productClassify
    }

    override fun getItemCount(): Int = dataList.size

    fun setDataList(data: List<ClassifyViewBean>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

}