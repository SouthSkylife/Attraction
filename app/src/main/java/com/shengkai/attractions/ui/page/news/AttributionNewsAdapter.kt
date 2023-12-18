package com.shengkai.attractions.ui.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shengkai.attractions.data.remote.AttractionData
import com.shengkai.attractions.databinding.ItemAttributionNewsBinding

class AttributionNewsAdapter (
    private val onAttractionClick: (String) -> Unit
) : RecyclerView.Adapter<AttributionNewsAdapter.AttractionViewHolder>() {

    private var attractionList: List<AttractionData> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setAttractionList(list: List<AttractionData>) {
        attractionList = list
        notifyDataSetChanged()
    }

    // define ViewHolder
    class AttractionViewHolder(private val binding: ItemAttributionNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // insert data to view
        fun bind(attraction: AttractionData, onAttractionClick: (String) -> Unit) {
            binding.tvNewsTitle.text = attraction.title
            binding.tvNewsContent.text = attraction.description

            binding.llNewsCard.setOnClickListener {
                onAttractionClick(attraction.url)
            }
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttractionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemAttributionNewsBinding.inflate(inflater, parent, false)
        return AttractionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AttractionViewHolder, position: Int) {
        holder.bind(attractionList[position], onAttractionClick)
    }

    override fun getItemCount(): Int {
        return attractionList.size
    }
}