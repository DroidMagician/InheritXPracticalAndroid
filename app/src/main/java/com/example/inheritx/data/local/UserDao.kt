package com.example.inheritx.data.local

import androidx.room.*
import com.example.inheritx.domain.firebase.model.FirebaseUserModel


@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    suspend fun getAll(): List<FirebaseUserModel>
        
    @Insert
    suspend fun insert(user: FirebaseUserModel)

    @Query("SELECT * FROM user WHERE primary_id = :primary_id")
    fun getUser(primary_id: Int?): FirebaseUserModel?

    @Update
    fun updateUser(user: FirebaseUserModel?)


    @Delete
    suspend fun delete(user: FirebaseUserModel)
      
}