package com.dicoding.kasmee.util

import com.dicoding.kasmee.data.model.response.cash.Cash
import com.google.gson.Gson

object SerializerHelper {

    // Serialize a single object.
    fun serializeToJson(cash: Cash?): String? {
        val gson = Gson()
        return gson.toJson(cash)
    }

    // Deserialize to single object.
    fun deserializeFromJson(jsonString: String?): Cash? {
        val gson = Gson()
        return gson.fromJson(jsonString, Cash::class.java)
    }
}