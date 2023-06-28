package com.example.inheritx.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.inheritx.domain.firebase.model.FirebaseUserModel
import com.example.inheritx.domain.firebase.model.RegisterRequestModel
import com.example.inheritx.domain.homeData.model.ArticleModel

@Database(
    entities = [
        FirebaseUserModel::class,
        ArticleModel::class,
    ],
    version = 1, exportSchema = false
)

@TypeConverters(value = [SourceTypeConvertor::class, ])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao?
    abstract fun getArticleDao(): ArticleDao?

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database

            var instance = INSTANCE

            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "gourmet_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
            }
            return instance


        }
    }
}

