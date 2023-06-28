package com.example.inheritx.domain.homeData.repository

import com.example.inheritx.data.base.WrappedListResponse
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.homeData.model.ArticleModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface of HomeRepository to expose to other module
 */
interface HomeRepository {

    /**
     * Method to getArticles from Repository
     * @return Flow of Outputs with ArticleList
     */
    suspend fun getArticles(): Flow<Output<WrappedListResponse<ArticleModel>>>
}