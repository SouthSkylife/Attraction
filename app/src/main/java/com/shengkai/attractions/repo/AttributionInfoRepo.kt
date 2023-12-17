package com.shengkai.attractions.repo

import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.shengkai.attractions.data.ApiService
import com.shengkai.attractions.data.AttractionInfoModel
import com.shengkai.attractions.data.AttributionNewsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * 景點資料存取介面(採用 OkHttp3，Retrofit for kotlin 建構中)
 */
class AttributionInfoRepo {

    /**
     * 取得最新消息(設定2023今年)
     */
    fun getAttributionNews(
        language: String,
        attributionNewsModel: MutableLiveData<AttributionNewsModel>
    ) {
        runBlocking {
            launch(Dispatchers.IO) {
                val url =
                    "https://www.travel.taipei/open-api/$language/Events/News".toHttpUrlOrNull()
                        ?.newBuilder()
                        ?.addQueryParameter("begin", "2023-01-01")
                        ?.addQueryParameter("end", "2023-12-31")
                        ?.addQueryParameter("page", "1")
                        ?.build()

                val client = OkHttpClient()
                val request = url?.let {
                    Request.Builder()
                        .url(it)
                        .addHeader("Accept", "application/json")
                        .build()
                }

                request?.let {
                    client.newCall(it).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                            response.use {
                                if (response.isSuccessful) {
                                    // 處理 API 響應，這裡你可以獲取 JSON 數據並進行相應的處理
                                    val responseBody = response.body?.string()

                                    val type = object : TypeToken<AttributionNewsModel>() {}.type
                                    val attractionInfo: AttributionNewsModel =
                                        Gson().fromJson(responseBody, type)

                                    println("消息資料：${Gson().toJson(attractionInfo)}")

                                    attributionNewsModel.postValue(attractionInfo)
                                }
                            }
                        }
                    })
                }
            }
        }
    }

    /**
     * 取得台北景點清單
     */
    fun getAttributionList(
        language: String,
        page : Int,
        attractionInfoData: MutableLiveData<AttractionInfoModel>
    ) {
        runBlocking {
            launch(Dispatchers.IO) {
                val url =
                    "https://www.travel.taipei/open-api/$language/Attractions/All".toHttpUrlOrNull()
                        ?.newBuilder()
                        ?.addQueryParameter("page", page.toString())
                        ?.build()

                val client = OkHttpClient()
                val request = url?.let {
                    Request.Builder()
                        .url(it)
                        .addHeader("Accept", "application/json")
                        .build()
                }

                request?.let {
                    client.newCall(it).enqueue(object : okhttp3.Callback {
                        override fun onFailure(call: okhttp3.Call, e: IOException) {
                            e.printStackTrace()
                        }

                        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                            response.use {
                                if (response.isSuccessful) {
                                    // 處理 API 響應，這裡你可以獲取 JSON 數據並進行相應的處理
                                    val responseBody = response.body?.string()

                                    val type = object : TypeToken<AttractionInfoModel>() {}.type
                                    val attractionInfo: AttractionInfoModel =
                                        Gson().fromJson(responseBody, type)

                                    println("景點資料：${Gson().toJson(attractionInfo)}")

                                    attractionInfoData.postValue(attractionInfo)
                                }
                            }
                        }
                    })
                }
            }
        }
    }


}