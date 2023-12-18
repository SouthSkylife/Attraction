package com.shengkai.attractions.ui.news

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shengkai.attractions.data.remote.AttractionNewsData
import com.shengkai.attractions.databinding.ItemAttributionNewsBinding

class AttributionNewsAdapter (
    private val onAttractionNewsClick: (String) -> Unit
) : RecyclerView.Adapter<AttributionNewsAdapter.AttractionViewHolder>() {

    private var attractionNewsList: List<AttractionNewsData> = emptyList()

    // define ViewHolder
    class AttractionViewHolder(private val binding: ItemAttributionNewsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // insert data to view
        fun bind(attraction: AttractionNewsData, onAttractionClick: (String) -> Unit) {
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
        holder.bind(attractionNewsList[position], onAttractionNewsClick)
    }

    override fun getItemCount(): Int {
        return attractionNewsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setAttractionNews(list: List<AttractionNewsData>) {
        attractionNewsList = list
        notifyDataSetChanged()
    }
}