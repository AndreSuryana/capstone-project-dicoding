package com.dicoding.kasmee.data.model.response.cash

import com.google.gson.annotations.SerializedName

data class Cash(

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("nama")
    val name: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("target")
    val target: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,
)