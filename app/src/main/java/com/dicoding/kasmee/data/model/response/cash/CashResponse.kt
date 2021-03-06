package com.dicoding.kasmee.data.model.response.cash

import com.google.gson.annotations.SerializedName

data class CashResponse(

    @SerializedName("current_page")
    val currentPage: Int,

    @SerializedName("data")
    val listCash: List<Cash>,

    @SerializedName("first_page_url")
    val firstPageUrl: String,

    @SerializedName("from")
    val from: Int,

    @SerializedName("last_page")
    val lastPage: Int,

    @SerializedName("last_page_url")
    val lastPageUrl: String,

    @SerializedName("links")
    val links: List<Links>,

    @SerializedName("next_page_url")
    val nextPageUrl: String,

    @SerializedName("path")
    val path: String,

    @SerializedName("per_page")
    val perPage: Int,

    @SerializedName("prev_page_url")
    val prevPageUrl: String,

    @SerializedName("to")
    val to: Int,

    @SerializedName("total")
    val total: Int,
)