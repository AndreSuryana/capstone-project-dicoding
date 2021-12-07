package com.dicoding.kasmee.data.model.response.transaction

import com.google.gson.annotations.SerializedName

data class Transaction(

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_kas")
    val idKas: Int,

    @SerializedName("id_user")
    val idUser: Int,

    @SerializedName("pemasukan")
    val pemasukan: Int,

    @SerializedName("pengeluaran")
    val pengeluaran: Int,

    @SerializedName("keuntungan")
    val keuntungan: Int,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,
)