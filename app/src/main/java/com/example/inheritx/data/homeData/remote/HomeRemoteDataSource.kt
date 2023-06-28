package com.example.inheritx.data.homeData.remote

import com.example.inheritx.data.base.BaseRemoteDataSource
import com.example.inheritx.data.base.WrappedListResponse
import com.example.inheritx.data.base.WrappedResponse
import com.example.inheritx.domain.common.Output
import com.example.inheritx.data.services.ApiService
import com.example.inheritx.domain.homeData.model.ArticleModel
import retrofit2.Retrofit
import javax.inject.Inject


/**
 * RemoteDataSource of UserList which we are getting from Assets Json File
 */
class HomeRemoteDataSource @Inject constructor(
    private val apiService: ApiService, retrofit: Retrofit
): BaseRemoteDataSource(retrofit){

    /**
     * Method to fetch the characters from CharsRemoteDataSource
     * @return Outputs with list of Characters
     */

    suspend fun getArticles(): Output<WrappedListResponse<ArticleModel>> {
        return getResponse(
            request = { apiService.getArticles() },
            defaultErrorMessage = "Error fetching Articles"
        )
    }
}