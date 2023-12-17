package com.shengkai.attractions.common.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.shengkai.attractions.R
import com.shengkai.attractions.common.constant.LanguageEnum
import com.shengkai.attractions.data.AttractionDetail
import com.shengkai.attractions.databinding.ItemAttributionDetailBinding
import com.shengkai.attractions.databinding.ItemLanguageBinding
import com.shengkai.attractions.util.GlideLoadUtil

class LanguageSelectAdapter(
    private val onLanguageClick: (LanguageEnum) -> Unit
) : RecyclerView.Adapter<LanguageSelectAdapter.LanguageSelectViewHolder>() {

    private val languageList: List<LanguageEnum> = enumValues<LanguageEnum>().toList()


    // define ViewHolder
    class LanguageSelectViewHolder(private val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // insert data to view
        fun bind(languageSign: LanguageEnum, onLanguageClick: (LanguageEnum) -> Unit) {
            binding.tvLanguage.text = languageSign.languageName

            binding.tvLanguage.setOnClickListener {
                onLanguageClick(languageSign)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageSelectViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLanguageBinding.inflate(inflater, parent, false)
        return LanguageSelectViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LanguageSelectViewHolder, position: Int) {
        holder.bind(languageList[position], onLanguageClick)
    }

    override fun getItemCount(): Int {
        return languageList.size
    }
}