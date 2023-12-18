package com.shengkai.attractions.ui.controller

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shengkai.attractions.data.local.ApplicationSp
import com.shengkai.attractions.data.remote.AttractionInfoModel
import com.shengkai.attractions.data.remote.AttributionNewsModel
import com.shengkai.attractions.repo.AttributionInfoRepo

class MainActivityViewModel : ViewModel() {
    var attractionInfoPage: Int = 1
    var attractionInfoData: MutableLiveData<AttractionInfoModel> = MutableLiveData()
    var attributionNewsData: MutableLiveData<AttributionNewsModel> = MutableLiveData()

    private val repo = AttributionInfoRepo()

    /**
     * 取得消息列表資料
     */
    fun getAttributionNews(context: Context) {
        val language = ApplicationSp(context).getString(ApplicationSp.CURRENT_LANGUAGE_SIGN,"zh-tw")
        repo.getAttributionNews(language, attributionNewsData)
    }

    /**
     * 取得景點列表資料
     */
    fun getAttributionList(context: Context) {
        val language = ApplicationSp(context).getString(ApplicationSp.CURRENT_LANGUAGE_SIGN,"zh-tw")
        repo.getAttributionList(language, attractionInfoPage, attractionInfoData)
    }
}