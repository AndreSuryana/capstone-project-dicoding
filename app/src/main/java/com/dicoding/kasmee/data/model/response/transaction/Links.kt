package com.dicoding.kasmee.data.model.response.transaction

import com.google.gson.annotations.SerializedName

data class Links(

    @SerializedName("active")
    val active: Boolean,

    @SerializedName("label")
    val label: String,

    @SerializedName("url")
    val url: String
)