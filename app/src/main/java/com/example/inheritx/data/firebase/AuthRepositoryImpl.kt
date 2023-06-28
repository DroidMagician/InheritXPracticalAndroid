package com.example.inheritx.data.firebase

import com.example.inheritx.data.firebase.utils.await
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.firebase.model.FirebaseUserModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import javax.inject.Inject


class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override val currentUser: FirebaseUser?
        get() = firebaseAuth.currentUser

    override suspend fun login(email: String, password: String): Output<FirebaseUser> {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Output.success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Output.error(e.message ?: "", null)
        }
    }



    override suspend fun signup(
        name: String,
        email: String,
        password: String
    ): Output<FirebaseUser> {
        return try {
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()

            result?.user?.updateProfile(
                UserProfileChangeRequest.Builder().setDisplayName(name).build()
            )?.await()
            result?.user?.sendEmailVerification()
                ?.addOnCompleteListener(OnCompleteListener<Void?> { task ->

                })
            Output.success(result.user!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Output.error(e.message ?: "", null)
        }
    }

    override suspend fun editProfile(
        name: String,
        oldEmail: String,
        oldPassword: String,
        newEmail: String,
        newPassword: String
    ): Output<FirebaseUserModel> {
        return try {

            firebaseAuth.signInWithEmailAndPassword(oldEmail, oldPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            // Sign in success now update email
                            task.result.user!!.updateEmail(newEmail)
                                .addOnCompleteListener { task1 ->

                                }
                            task.result.user!!.updatePassword(newPassword).addOnCompleteListener {

                            }
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            task.result.user!!.updateProfile(profileUpdates)
                                .addOnCompleteListener {
                                    if(task.isSuccessful)
                                    {
                                        FirebaseAuth.getInstance().getCurrentUser()?.reload()
                                    }
                                }

                        }

                    }

            val firebaseUserModel = FirebaseUserModel()
            firebaseUserModel.name = name
            firebaseUserModel.email = newEmail
            firebaseUserModel.password = newPassword
            Output.success(firebaseUserModel)
        } catch (e: Exception) {
            e.printStackTrace()
            Output.error(e.message ?: "", null)
        }
    }

    override fun logout() {
        firebaseAuth.signOut()
    }
}