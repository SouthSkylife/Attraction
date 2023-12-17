package com.shengkai.attractions.data

import com.google.gson.annotations.SerializedName

data class AttractionInfoModel(
    val total: Int,
    val data: List<AttractionDetail>
)

data class AttractionDetail(
    val id: Int,
    val name: String,
    val introduction: String,
    @SerializedName("open_time")
    val openTime:String,
    val zipcode: String,
    val address: String,
    val tel: String,
    val fax: String?,
    val email: String?,
    val months: String,
    val facebook: String?,
    val ticket: String,
    val remind: String,
    val modified: String,
    val url: String,
    val category: List<Category>,
    val target: List<Target>,
    val service: List<Service>,
    val friendly: List<Friendly>,
    val images: List<Image>,
    val files: List<Any>,
    val links: List<Any>
)

data class Category(
    val id: Int,
    val name: String
)

data class Target(
    val id: Int,
    val name: String
)

data class Service(
    val id: Int,
    val name: String
)

data class Friendly(
    val id: Int,
    val name: String
)

data class Image(
    val src: String,
    val subject: String,
    val ext: String
)
