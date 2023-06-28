package com.example.inheritx.data.homeData.repository
import android.util.Log
import com.example.inheritx.data.base.WrappedListResponse
import com.google.gson.Gson
import com.example.inheritx.data.homeData.remote.HomeRemoteDataSource
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.homeData.model.ArticleModel
import com.example.inheritx.domain.homeData.repository.HomeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

/**
 * Implementation of HomeRepository
 * @param homeRemoteDataSource the object of remote data source
 */
internal class HomeRepositoryImpl @Inject constructor(
    private val homeRemoteDataSource: HomeRemoteDataSource
) : HomeRepository {
    override suspend fun getArticles(): Flow<Output<WrappedListResponse<ArticleModel>>> {
        return flow {
            emit(Output.loading())
            val result = homeRemoteDataSource.getArticles();
            emit(result)
        }.flowOn(Dispatchers.IO)
    }
}