package com.example.inheritx.presentation.firebaseLogin

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inheritx.R
import com.example.inheritx.data.firebase.AuthRepository
import com.example.inheritx.data.local.AppDatabase
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.firebase.model.FirebaseUserModel
import com.example.inheritx.domain.firebase.model.LoginRequestModel
import com.example.inheritx.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FirebaseLoginViewModel @Inject constructor(
    private val repository: AuthRepository,
    private val application: Application
) :
//constructor injection
    BaseViewModel() {
    var loginRequestModel = LoginRequestModel()
    private val _loginResponse = MutableLiveData<Output<FirebaseUser>>()
    val loginResponse: LiveData<Output<FirebaseUser>> = _loginResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    //Do Login With Firebase
    fun login() = viewModelScope.launch {
        if (validateInput()) {
            _loginResponse.value = Output(Output.Status.LOADING)
            val result =
                repository.login(loginRequestModel.email ?: "", loginRequestModel.password ?: "")
            _loginResponse.value = result
        }

}
    fun addUserToDatabase(firebaseUserModel: FirebaseUserModel)
    {
        viewModelScope.launch {
            AppDatabase.getDatabase(application).getUserDao()?.insert(firebaseUserModel)
        }
    }
    //Validate User Input
private fun validateInput(): Boolean {
    if (loginRequestModel.email.isNullOrBlank()) {
        _errorMessage.value = application.getString(R.string.please_enter_email)
        return false
    }
    else if (!Patterns.EMAIL_ADDRESS.matcher(loginRequestModel.email.toString().trim()).matches()) {
        _errorMessage.value = application.getString(R.string.enter_valid_email)
        return false
    }
    else if (loginRequestModel.password.isNullOrBlank()) {
        _errorMessage.value = application.getString(R.string.please_enter_password)
        return false
    } else {
        return true
    }
}
}