package com.example.inheritx.presentation.firebaseRegister

import android.view.View
import com.example.inheritx.R
import com.example.inheritx.databinding.ActivityFirebaseRegisterBinding
import com.example.inheritx.domain.common.Output
import com.example.inheritx.presentation.base.BaseActivity
import com.example.inheritx.presentation.firebaseLogin.FirebaseLoginActivity
import com.example.inheritx.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint


/**
 * Launcher Activity (Entry point) of this application
 */

@AndroidEntryPoint
class FirebaseRegisterActivity :
    BaseActivity<FirebaseRegisterViewModel, ActivityFirebaseRegisterBinding>(
        ActivityFirebaseRegisterBinding::inflate
    ) {

    override fun initActivity() {
        listener()
        observe()
    }

    private fun listener() {
        myBinding.txtLogin.setOnClickListener {
            startActivity(FirebaseLoginActivity::class.java)
            finish()
        }
        myBinding.btnRegister.setOnClickListener {
            viewModel.signup()
        }

    }


    private fun observe() {

        viewModel.errorMessage.observe(this) { result ->
            showMessage(result)
        }
        viewModel.registerResponse.observe(this) { result ->

            when (result.status) {
                Output.Status.SUCCESS -> {
                    myBinding.progressBar.visibility = View.GONE
                    result.data?.let { userObj ->
                        showMessage(getString(R.string.message_register_success))
                        startActivity(FirebaseLoginActivity::class.java)
                        finish()
                    }

                }
                Output.Status.GO_NEXT ->
                {
                    finish()
                    startActivity(HomeActivity::class.java)
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