package com.example.inheritx.data.services



import com.example.inheritx.data.base.Config
import com.example.inheritx.data.base.WrappedListResponse
import com.example.inheritx.domain.homeData.model.ArticleModel
import retrofit2.Response
import retrofit2.http.*

/**
 * Retrofit API Service
 */
interface ApiService {

    @GET(Config.GET_ARTICLES)
    suspend fun getArticles(): Response<WrappedListResponse<ArticleModel>>

}