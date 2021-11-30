package com.dicoding.kasmee.data.model.response.auth

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("email_verified_at")
    val emailVerifiedAt: String? = null,

    @SerializedName("profile_photo_path")
    val profilePhotoPath: String? = null,

    @SerializedName("phone_number")
    val phoneNumber: String? = null,

    @SerializedName("roles")
    val roles: String? = null,

    @SerializedName("created_at")
    val createdAt: String? = null,

    @SerializedName("updated_at")
    val updatedAt: String? = null,

    @SerializedName("profile_photo_url")
    val profilePhotoUrl: String? = null,
)