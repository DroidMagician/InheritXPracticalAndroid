package com.example.inheritx.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.inheritx.domain.homeData.model.ArticleModel

@Dao
interface ArticleDao {
  
    @Query("SELECT * FROM Article")
    suspend fun getAll(): List<ArticleModel>
    
    @Insert
    suspend fun insertAll(articleList: List<ArticleModel>)

    @Query("DELETE FROM Article WHERE title = :title")
    suspend fun delete(title: String)
      
}