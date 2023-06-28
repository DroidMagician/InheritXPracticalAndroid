package com.example.inheritx.presentation.firebaseLogin

import android.util.Log
import android.view.View
import com.example.inheritx.R
import com.example.inheritx.databinding.ActivityFirebaseLoginBinding
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.firebase.model.FirebaseUserModel
import com.example.inheritx.presentation.base.BaseActivity
import com.example.inheritx.utils.SharedPrefs
import com.example.inheritx.presentation.firebaseRegister.FirebaseRegisterActivity
import com.example.inheritx.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FirebaseLoginActivity : BaseActivity<FirebaseLoginViewModel, ActivityFirebaseLoginBinding>(ActivityFirebaseLoginBinding::inflate){

    //Field injection
    @Inject
    lateinit var pref: SharedPrefs

    override fun initActivity() {
        observe()
        listner()
    }

    private fun listner() {
        myBinding.txtRegisterUser.setOnClickListener {
            startActivity(FirebaseRegisterActivity::class.java)
            finish()
        }
        myBinding.btnLogin.setOnClickListener {
            viewModel.login()
        }
    }

    private fun observe() {

        viewModel.errorMessage.observe(this) { result ->
            showMessage(result)
        }

        viewModel.loginResponse.observe(this) { result ->

            when (result.status) {
                Output.Status.SUCCESS -> {
                    myBinding.progressBar.visibility = View.GONE

                    result.data?.let { list ->
                        if(!list.isEmailVerified)
                        {
                            showMessage(getString(R.string.please_verify_your_email))
                        }
                        else{
                            var firebaseUserModel = FirebaseUserModel()
                            firebaseUserModel.apply {
                                this.name = list.displayName
                                this.email = list.email
                                this.password = viewModel.loginRequestModel.password
                            }
                            pref.storeLoginDetail(firebaseUserModel)
                            viewModel.addUserToDatabase(firebaseUserModel)
                            finish()
                            startActivity(HomeActivity::class.java)
                        }

                    }
                }
                Output.Status.ERROR -> {
                    myBinding.progressBar.visibility = View.GONE
                    result.message?.let {
                        showMessage(result.message)
                    }
                }
                Output.Status.LOADING -> {
                    myBinding.progressBar.visibility = View.VISIBLE
                }
                else -> {
                    //No need to handle for this
                }
            }
        }
    }
}