package com.hurryyu.tworecyclerviewlinkage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.hurryyu.tworecyclerviewlinkage.bean.ProductInfoViewBean
import com.hurryyu.tworecyclerviewlinkage.databinding.ItemProductBinding

class ProductInfoAdapter : RecyclerView.Adapter<ProductInfoAdapter.ViewHolder>() {

    private val differ = AsyncListDiffer<ProductInfoViewBean>(this,
        object : DiffUtil.ItemCallback<ProductInfoViewBean?>() {
            override fun areItemsTheSame(
                oldItem: ProductInfoViewBean,
                newItem: ProductInfoViewBean
            ): Boolean = oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: ProductInfoViewBean,
                newItem: ProductInfoViewBean
            ): Boolean = oldItem == newItem

            private val payloadResult = Any()
            override fun getChangePayload(
                oldItem: ProductInfoViewBean,
                newItem: ProductInfoViewBean
            ): Any {
                return payloadResult
            }
        })

    class ViewHolder(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(bean: ProductInfoViewBean) {
            with(binding) {
                tvDescribe.text = bean.describe
                tvPrice.text = "¥ ${bean.price}"
                tvProduct.text = bean.title
                ivProduct.load(bean.imgUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int = differ.currentList.size

    fun submitList(list: List<ProductInfoViewBean>) {
        differ.submitList(list)
    }

    fun isGroupHeader(position: Int): Boolean = differ.currentList[position].isGroupFirst

    fun getGroupName(position: Int): String = differ.currentList[position].groupName
}