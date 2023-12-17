package com.shengkai.attractions.ui.controller

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shengkai.attractions.data.AttractionInfoModel
import com.shengkai.attractions.data.AttributionNewsModel
import com.shengkai.attractions.repo.AttributionInfoRepo

class MainActivityViewModel : ViewModel() {
    var attractionInfoPage: Int = 0
    var attractionInfoData: MutableLiveData<AttractionInfoModel> = MutableLiveData()
    var attributionNewsData: MutableLiveData<AttributionNewsModel> = MutableLiveData()

    private val repo = AttributionInfoRepo()

    fun getAttributionList() {
        attractionInfoPage++
        repo.getAttributionList("zh-tw", attractionInfoPage, attractionInfoData)
    }

    fun getAttributionNews() {
        repo.getAttributionNews("zh-tw", attributionNewsData)
    }
}