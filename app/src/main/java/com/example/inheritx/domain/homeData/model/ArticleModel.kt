package com.example.inheritx.domain.homeData.model

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Keep
@Parcelize
@Entity(tableName = "Article")
data class ArticleModel(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var primary_id: Int = 0,
    @SerializedName("source") var source: Source? = Source(),
    @SerializedName("author") var author: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("url") var url: String? = null,
    @SerializedName("urlToImage") var urlToImage: String? = null,
    @SerializedName("publishedAt") var publishedAt: String? = null,
    @SerializedName("content") var content: String? = null
) : Parcelable

@Parcelize
data class Source(
    @SerializedName("id") var id: String? = null,
    @SerializedName("name") var name: String? = null
) : Parcelable