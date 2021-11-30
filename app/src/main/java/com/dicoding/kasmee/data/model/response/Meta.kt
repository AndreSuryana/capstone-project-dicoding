package com.dicoding.kasmee.data.model.response

import com.google.gson.annotations.SerializedName

data class Meta(

    @SerializedName("code")
    val code: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("status")
    val status: String
)