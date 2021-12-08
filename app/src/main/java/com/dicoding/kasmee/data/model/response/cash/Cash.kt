package com.dicoding.kasmee.data.model.response.cash

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cash(

    @SerializedName("id")
    val id: Int,

    @SerializedName("id_user")
    val userId: Int,

    @SerializedName("nama")
    val name: String,

    @SerializedName("slug")
    val slug: String,

    @SerializedName("target")
    val target: Long,

    @SerializedName("created_at")
    val createdAt: String,

    @SerializedName("updated_at")
    val updatedAt: String,

    @SerializedName("total_pemasukan")
    val totalIncome: Long,

    @SerializedName("total_pengeluaran")
    val totalOutcome: Long,

    @SerializedName("total_keuntungan")
    val totalProfit: Long
) : Parcelable