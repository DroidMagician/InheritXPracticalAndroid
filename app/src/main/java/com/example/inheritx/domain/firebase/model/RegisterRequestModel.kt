package com.example.inheritx.domain.firebase.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class RegisterRequestModel(
    @SerializedName("password") var password: String  = "",
    @SerializedName("name") var name: String = "",
    @SerializedName("email") var email: String = "",
)


