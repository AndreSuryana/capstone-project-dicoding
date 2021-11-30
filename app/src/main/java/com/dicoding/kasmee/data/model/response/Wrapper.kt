package com.dicoding.kasmee.data.model.response

import com.google.gson.annotations.SerializedName

data class Wrapper<T>(

    @SerializedName("data")
    val data: T,

    @SerializedName("meta")
    val meta: Meta? = null,
)
