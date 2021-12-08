package com.dicoding.kasmee.data.model.response.transaction

import com.google.gson.annotations.SerializedName

data class Transaction(

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_kas")
    val cashId: Int,

    @SerializedName("id_user")
    val userId: Int,

    @SerializedName("pemasukan")
    val income: Long,

    @SerializedName("pengeluaran")
    val outcome: Long,

    @SerializedName("keuntungan")
    val profit: Long,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("keterangan")
    val description: String

)