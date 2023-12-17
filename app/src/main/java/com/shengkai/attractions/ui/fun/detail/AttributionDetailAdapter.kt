package com.shengkai.attractions.ui.detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shengkai.attractions.R
import com.shengkai.attractions.data.AttractionData
import com.shengkai.attractions.data.AttractionDetail
import com.shengkai.attractions.databinding.ItemAttributionDetailBinding
import com.shengkai.attractions.databinding.ItemAttributionNewsBinding
import com.shengkai.attractions.ui.news.AttributionNewsAdapter
import com.shengkai.attractions.util.GlideLoadUtil

class AttributionDetailAdapter(
    private val onAttractionClick: (AttractionDetail) -> Unit
) : RecyclerView.Adapter<AttributionDetailAdapter.AttractionDetailViewHolder>() {

    private var attractionList: List<AttractionDetail> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setAttractionDetailList(list: List<AttractionDetail>) {
        attractionList = list
        notifyDataSetChanged()
    }

    // define ViewHolder
    class AttractionDetailViewHolder(private val binding: ItemAttributionDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // insert data to view
        fun bind(detail: AttractionDetail, onAttractionClick: (AttractionDetail) -> Unit) {
            binding.tvAttributionTitle.text = detail.name
            binding.tvAttributionContent.text = detail.introduction


            if (detail.images.isEmpty()) {
                binding.ivAttribution.setImageResource(R.drawable.ic_taipei_visitor_logo)

            } else {
                GlideLoadUtil.loadImageWithCenterCrop(
                    binding.root.context,
                    detail.images[0].src,
                    binding.ivAttribution
                )
            }

            binding.ivShowDetail.setOnClickListener {
                onAttractionClick(detail)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionDetailViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttributionDetailBinding.inflate(inflater, parent, false)
        return AttractionDetailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttractionDetailViewHolder, position: Int) {
        holder.bind(attractionList[position], onAttractionClick)
    }

    override fun getItemCount(): Int {
        return attractionList.size
    }
}