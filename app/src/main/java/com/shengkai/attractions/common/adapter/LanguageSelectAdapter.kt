package com.shengkai.attractions.common.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.shengkai.attractions.R
import com.shengkai.attractions.common.constant.LanguageEnum
import com.shengkai.attractions.data.local.ApplicationSp
import com.shengkai.attractions.databinding.ItemLanguageBinding

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

            val recentLanguage =
                ApplicationSp(binding.root.context).getString(ApplicationSp.CURRENT_LANGUAGE_SIGN)

            val context = binding.root.context
            val textColorWhite = ContextCompat.getColor(context, R.color.white)
            val textColorGrey = ContextCompat.getColor(context, R.color.color_moon_grey)

            val (bgColor, textColor) = if (languageSign.languageSign == recentLanguage) {
                R.drawable.shape_ocean_blue_tag_bg to textColorWhite
            } else {
                android.R.color.white to textColorGrey
            }

            binding.llLanguage.setBackgroundResource(bgColor)
            binding.tvLanguage.setTextColor(textColor)

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