package com.example.inheritx.presentation.home.updateProfile

import android.R
import android.util.Log
import android.view.MenuItem
import android.view.View
import com.example.inheritx.databinding.ActivityFirebaseUpdateProfileBinding
import com.example.inheritx.domain.common.Output
import com.example.inheritx.presentation.base.BaseActivity
import com.example.inheritx.presentation.firebaseRegister.FirebaseRegisterActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FirebaseUpdateProfileActivity : BaseActivity<FirebaseUpdateProfileViewModel, ActivityFirebaseUpdateProfileBinding>(ActivityFirebaseUpdateProfileBinding::inflate){



    override fun initActivity() {
        getSupportActionBar()?.setDisplayHomeAsUpEnabled(true);
        observe()
        listner()
    }

    private fun listner() {
        myBinding.btnUpdate.setOnClickListener {
           viewModel.editProfile()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() === R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun observe() {
        viewModel.errorMessage.observe(this) { result ->
            showMessage(result)
        }

        viewModel.loggedInUser.observe(this){
            myBinding.edtEmail.setText(it.email.toString())
            myBinding.edtName.setText(it.name.toString())
            myBinding.edtPassword.setText(it.password.toString())
        }

        viewModel.registerResponse.observe(this) { result ->

            when (result.status) {
                Output.Status.SUCCESS -> {
                    myBinding.progressBar.visibility = View.GONE
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
                Output.Status.GO_NEXT -> {
                    showMessage("Profile Updated Successfully")
                    finish()
                }
                else -> {
                    //No need to handle for this
                }
            }
        }
    }
}