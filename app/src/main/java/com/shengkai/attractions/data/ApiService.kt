package com.shengkai.attractions.data

import androidx.annotation.Keep
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Retrofit Service
 */
interface ApiService {

    /**
     * 取得景點列表資料
     */
    @GET("/{lang}/Attractions/All")
    @Headers("Accept: application/json")
     fun getAttractionsInfoAsync(
        @Path("lang") lang: String,
        @Query("page") page: Int
    ): Call<AttractionInfoModel>

    /**
     * 取得最新消息
     */
    @GET("/{lang}/Events/News")
    @Headers("Accept: application/json")
    fun getAttractionsNewsAsync(
        @Path("lang") lang: String,
        @Query("begin") begin: String,
        @Query("end") end: String,
        @Query("page") page: Int
    ): Call<AttractionInfoModel>

}