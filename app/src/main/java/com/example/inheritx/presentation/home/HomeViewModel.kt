package com.example.inheritx.presentation.home

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inheritx.data.base.WrappedListResponse
import com.example.inheritx.data.local.AppDatabase
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.homeData.model.ArticleModel
import com.example.inheritx.domain.homeData.usecase.HomeUseCase
import com.example.inheritx.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val homeUseCase: HomeUseCase, private val application: Application) : BaseViewModel() {


    private val _articleListResponse = MutableLiveData<List<ArticleModel>?>()
    val articleListResponse: MutableLiveData<List<ArticleModel>?> = _articleListResponse

    private val _otherResponse = MutableLiveData<Output<WrappedListResponse<ArticleModel>>>()
    val otherResponse: LiveData<Output<WrappedListResponse<ArticleModel>>> = _otherResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    init {
        getArticalList()
    }
    fun getArticalList() {
        _otherResponse.value = Output(Output.Status.LOADING)
        viewModelScope.launch {
            val articles = AppDatabase.getDatabase(application).getArticleDao()?.getAll()

            if((articles?.size ?: 0) > 0)
            {
                _articleListResponse.value = articles
            }
            else{
                homeUseCase.execute().collect {
                    _articleListResponse.value = it.data?.data
                    addArticlesToDatabase(it.data?.data)
                }
            }

        }
    }
    //Add All article to room Database
    fun addArticlesToDatabase(data: List<ArticleModel>?)
    {
        viewModelScope.launch {
            data?.let {
                AppDatabase.getDatabase(application).getArticleDao()?.insertAll(
                    it
                )
            }
        }
    }

    //Get All Article from Room Database
    fun getArticlesFromDatabase(): ArrayList<ArticleModel> {
        var myArticles = ArrayList<ArticleModel>()
        viewModelScope.launch {
            var articles = AppDatabase.getDatabase(application).getArticleDao()?.getAll()
            articles?.let { myArticles.addAll(it) }
        }
        return myArticles
    }

    //Delete Article from room database
    fun deleteArticlesFromDatabase(articleModel: ArticleModel) {

        viewModelScope.launch {
           articleModel.title?.let { AppDatabase.getDatabase(application).getArticleDao()?.delete(it) };
        }
    }

    //Clear All stored data from Table in Room Database
    fun clearAllTables()
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(application).clearAllTables()
            }
        }
    }
}