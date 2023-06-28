package com.example.inheritx.presentation.home

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import com.example.inheritx.BR
import com.example.inheritx.R
import com.example.inheritx.databinding.ActivityHomeBinding
import com.example.inheritx.domain.common.Output
import com.example.inheritx.domain.homeData.model.ArticleModel
import com.example.inheritx.presentation.base.BaseActivity
import com.example.inheritx.presentation.firebaseLogin.FirebaseLoginActivity
import com.example.inheritx.presentation.home.updateProfile.FirebaseUpdateProfileActivity
import com.example.inheritx.utils.BindingAdapter
import com.example.inheritx.utils.SharedPrefs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeActivity : BaseActivity<HomeViewModel, ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    //Field injection
    @Inject
    lateinit var pref: SharedPrefs
    var articleList: ArrayList<ArticleModel> = kotlin.collections.ArrayList<ArticleModel>()
    override fun initActivity() {
        observe()
        listner()
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.getItemId()) {
            R.id.menu1 -> startActivity(FirebaseUpdateProfileActivity::class.java)
            R.id.menu2 -> {
              displayLogoutDialog()
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun displayLogoutDialog() {
        MaterialAlertDialogBuilder(this@HomeActivity)
            .setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Yes") { _, _ ->
                // Respond to positive button press
                viewModel.clearAllTables()
                startActivity(FirebaseLoginActivity::class.java)
                finishAffinity()
            }
            .setNegativeButton("No") { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .show()

    }

    private fun listner() {

        myBinding.idSV.setOnClickListener { myBinding.idSV.isIconified = false }
        myBinding.idSV.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // on below line we are checking
                // if query exist or not.
                val myFilterList = ArrayList<ArticleModel>()
                val filterList = articleList.filter { query?.let { it1 -> it.title?.contains(it1,true) } == true
                        || query?.let { it1 -> it.description?.contains(it1,true) } == true}
                myFilterList.addAll(filterList)
                setArticleAdapter(myFilterList)
                myBinding.listArticles.adapter?.notifyDataSetChanged()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                val myFilterList = ArrayList<ArticleModel>()
                val filterList = articleList.filter { newText?.let { it1 -> it.title?.contains(it1,true) } == true
                        || newText?.let { it1 -> it.description?.contains(it1,true) } == true
                }
                myFilterList.addAll(filterList)
                setArticleAdapter(myFilterList)
                myBinding.listArticles.adapter?.notifyDataSetChanged()
                return false
            }
        })
    }

    private fun observe() {

        viewModel.errorMessage.observe(this) { result ->
            showMessage(result)
        }

        viewModel.articleListResponse.observe(this) { result ->
            result.let { it?.let { it1 -> articleList.addAll(it1) } }
            setArticleAdapter(articleList)
            if(articleList.size > 0)
            myBinding.progressBar.visibility = View.GONE

        }
        viewModel.otherResponse.observe(this) { result ->

            when (result.status) {
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

    private fun setArticleAdapter(list: ArrayList<ArticleModel>) {

        myBinding.listArticles.adapter = BindingAdapter(
            layoutId = R.layout.row_article,
            br = BR.model,
            list = list,
            clickListener = { view, position ->
                when (view.id) {
                    R.id.icDelete -> {
                        deleteArticleDialog(position,list)
                    }
                }
            })

    }

    private fun deleteArticleDialog(position: Int, list: ArrayList<ArticleModel>) {
       MaterialAlertDialogBuilder(this@HomeActivity)
            .setMessage("Are you sure you want to delete this Article?")
            .setPositiveButton("Delete") { _, _ ->
                // Respond to positive button press
                val articleModel = list.get(position)
                viewModel.deleteArticlesFromDatabase(articleModel)
                list.removeAt(position)
                myBinding.listArticles.adapter?.notifyDataSetChanged()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                // Respond to negative button press
                dialog.dismiss()
            }
            .show()
    }
}