package com.shengkai.attractions.data.remote

data class AttributionNewsModel(
    val total: Int,
    val data: List<AttractionData>
)

data class AttractionData(
    val id: Int,
    val title: String,
    val description: String,
    val begin: String?,
    val end: String?,
    val posted: String,
    val modified: String,
    val url: String,
    val files: List<Any>,
    val links: List<Link>
)

data class Link(
    val src: String,
    val subject: String
)
