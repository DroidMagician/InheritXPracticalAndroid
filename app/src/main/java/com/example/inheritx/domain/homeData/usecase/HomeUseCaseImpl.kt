package com.example.inheritx.domain.homeData.usecase
import com.example.inheritx.data.base.WrappedListResponse
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.homeData.model.ArticleModel
import com.example.inheritx.domain.homeData.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/**
 * Implementation of HomeUseCase
 * @param homeRepository the HomeRepository object
 */
internal class HomeUseCaseImpl @Inject constructor(private val homeRepository: HomeRepository) :
    HomeUseCase {

    override suspend fun execute(): Flow<Output<WrappedListResponse<ArticleModel>>> {
        return homeRepository.getArticles()
    }
}
