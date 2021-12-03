package com.dicoding.kasmee.data.model.response.cash

import com.dicoding.kasmee.data.model.response.auth.User
import com.google.gson.annotations.SerializedName

data class Cash(

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("id_transaksi")
    val idTransaksi: Int,

    @SerializedName("target")
    val target: Int,

    @SerializedName("transaksi")
    val transaksi: Transaction,

    @SerializedName("user")
    val user: User,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,
)