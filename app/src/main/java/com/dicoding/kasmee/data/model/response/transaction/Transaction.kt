package com.dicoding.kasmee.data.model.response.transaction

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
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

    @SerializedName("keterangan")
    val description: String,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    ) : Parcelable