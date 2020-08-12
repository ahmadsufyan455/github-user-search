package com.fynzero.finalsubmission.view.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.adapter.UserAdapter
import com.fynzero.finalsubmission.model.User
import com.fynzero.finalsubmission.viewmodel.UserSearchViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userSearchViewModel: UserSearchViewModel
    private val userList = ArrayList<User>()
    private val userAdapter = UserAdapter(userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        // setup recycle view
        rv_user.layoutManager = LinearLayoutManager(this)
        rv_user.setHasFixedSize(true)

        userAdapter.notifyDataSetChanged()
        rv_user.adapter = userAdapter

        setSearch()
        getViewModel()

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val detail = Intent(this@MainActivity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, user)
                startActivity(detail)
            }

        })

        btn_favorite.setOnClickListener {
            startActivity(Intent(this, FavoriteActivity::class.java))
        }

        btn_setting.setOnClickListener {
            startActivity(Intent(this, SettingPreferenceActivity::class.java))
        }
    }

    private fun setSearch() {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        search.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                stateQueryListener()
                userSearchViewModel.setUser(query)

                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                search.clearFocus()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

        })
    }

    private fun getViewModel() {
        userSearchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(UserSearchViewModel::class.java)

        userSearchViewModel.getUser().observe(this, Observer { users ->
            if ((users != null) && (users.size != 0)) {
                userAdapter.setUser(users)
                stateSuccess()
            } else {
                stateNoResult()
            }
        })

        userSearchViewModel.noConnection().observe(this, Observer { res ->
            no_internet_connection.visibility = View.VISIBLE
            Toast.makeText(this, res, Toast.LENGTH_SHORT).show()
        })
    }

    private fun stateNoResult() {
        img_search.visibility = View.INVISIBLE
        txt_search_desc.visibility = View.INVISIBLE
        rv_user.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
        no_result.visibility = View.VISIBLE
    }

    private fun stateSuccess() {
        img_search.visibility = View.INVISIBLE
        txt_search_desc.visibility = View.INVISIBLE
        progressBar.visibility = View.INVISIBLE
        rv_user.visibility = View.VISIBLE
    }

    private fun stateQueryListener() {
        progressBar.visibility = View.VISIBLE
        rv_user.visibility = View.INVISIBLE
        img_search.visibility = View.INVISIBLE
        txt_search_desc.visibility = View.INVISIBLE
        no_result.visibility = View.INVISIBLE
        no_internet_connection.visibility = View.INVISIBLE
    }
}