package com.erkankaplan.getlanipadressen.room._01_entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user_model")
data class UserModel(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("Id") val id: Int = 0,

    @SerializedName("UserName")
    val userName: String,

    @SerializedName("Password")
    val password: String,

    @SerializedName("Authorization")
    val authorization: Int

)
