package com.example.inheritx.data.firebase

import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.firebase.model.FirebaseUserModel
import com.google.firebase.auth.FirebaseUser

interface AuthRepository {
    val currentUser: FirebaseUser?
    suspend fun login(email: String, password: String): Output<FirebaseUser>
    suspend fun signup(
        name: String,
        email: String,
        password: String,
    ): Output<FirebaseUser>

    suspend fun editProfile(
        name: String,
        oldEmail: String,
        oldPassword: String,
        newEmail:String,
        newPassword:String
    ): Output<FirebaseUserModel>

    fun logout()
}