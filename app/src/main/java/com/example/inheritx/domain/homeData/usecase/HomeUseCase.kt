package com.example.inheritx.domain.homeData.usecase

import com.example.inheritx.data.base.WrappedListResponse
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.homeData.model.ArticleModel
import kotlinx.coroutines.flow.Flow

/**
 * Interface of HomeUseCase to expose to ui module
 */
interface HomeUseCase {
    /**
     * UseCase Method to getArticleList from Data Layer
     */
    suspend fun execute(): Flow<Output<WrappedListResponse<ArticleModel>>>
}