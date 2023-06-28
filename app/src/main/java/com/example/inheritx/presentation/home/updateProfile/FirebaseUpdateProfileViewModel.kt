package com.example.inheritx.presentation.home.updateProfile

import android.app.Application
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.inheritx.R
import com.example.inheritx.data.firebase.AuthRepository
import com.example.inheritx.data.local.AppDatabase
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.firebase.model.FirebaseUserModel
import com.example.inheritx.domain.firebase.model.RegisterRequestModel
import com.example.inheritx.presentation.base.BaseViewModel
import com.google.firebase.auth.FirebaseUser
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class FirebaseUpdateProfileViewModel @Inject constructor(private val repository: AuthRepository, private val application: Application) :
    BaseViewModel() {
    var registerRequestModel = RegisterRequestModel()

    private val _registerResponse = MutableLiveData<Output<FirebaseUserModel>>()
    val registerResponse: LiveData<Output<FirebaseUserModel>> = _registerResponse

    private val _loggedInUser = MutableLiveData<FirebaseUserModel>()
    val loggedInUser: LiveData<FirebaseUserModel> = _loggedInUser

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    var oldUserModel:FirebaseUserModel ? = null
    init {
        getUserFromDatabase()
    }

    private fun getUserFromDatabase() {
        viewModelScope.launch {
           var userList = AppDatabase.getDatabase(application).getUserDao()?.getAll();
            if((userList?.size ?: 0) > 0)
            {
                oldUserModel = userList?.first()
                _loggedInUser.value = userList?.first()
            }
        }
    }

    //Do edit profile of User in Firebase
    fun editProfile() = viewModelScope.launch {
        if(validateInput())
        {
            _registerResponse.value = Output(Output.Status.LOADING)
            val result = repository.editProfile(registerRequestModel.name, oldUserModel?.email ?: "",oldUserModel?.password ?: "",registerRequestModel.email, registerRequestModel.password)
            _registerResponse.value =  Output(Output.Status.GO_NEXT)
            result.data?.let { updateUserToDatabase(it) }
        }
    }

    //Update User data in local database
    fun updateUserToDatabase(firebaseUserModel: FirebaseUserModel)
    {
        viewModelScope.launch {
            withContext(Dispatchers.IO)
            {
                val id = oldUserModel?.primary_id
                if (id != null) {
                    firebaseUserModel.primary_id = id
                }
                AppDatabase.getDatabase(application).getUserDao()?.updateUser(firebaseUserModel)
            }

        }
    }


    //Validate User Input
    private fun validateInput(): Boolean {
        if(registerRequestModel.name.isNullOrBlank())
        {
            _errorMessage.value = application.getString(R.string.please_enter_name)
            return false
        }
        else if(registerRequestModel.email.isNullOrBlank())
        {
            _errorMessage.value = application.getString(R.string.please_enter_email)
            return false
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(registerRequestModel.email.trim()).matches()) {
            _errorMessage.value = application.getString(R.string.enter_valid_email)
            return false
        }
        else if(registerRequestModel.password.isNullOrBlank())
        {
            _errorMessage.value = application.getString(R.string.please_enter_password)
            return false
        }
        else return true
    }
}