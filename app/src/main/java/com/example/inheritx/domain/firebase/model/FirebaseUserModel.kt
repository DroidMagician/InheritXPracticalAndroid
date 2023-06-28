package com.example.inheritx.domain.firebase.model

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "user")
data class FirebaseUserModel(
   @PrimaryKey(autoGenerate = true)
   @NonNull
   var primary_id: Int = 0,
   @SerializedName("email") var email: String? = null,
   @SerializedName("name") var name: String? = null,
   @SerializedName("password") var password: String? = null,
)


