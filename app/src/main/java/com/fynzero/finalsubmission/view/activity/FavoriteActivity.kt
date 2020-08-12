package com.fynzero.finalsubmission.view.activity

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.adapter.UserAdapter
import com.fynzero.finalsubmission.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.fynzero.finalsubmission.helper.MappingHelper
import com.fynzero.finalsubmission.model.User
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private val userList = ArrayList<User>()
    private val userAdapter = UserAdapter(userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        // setup recycle view
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)

        userAdapter.notifyDataSetChanged()
        rv_favorite.adapter = userAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        // get data
        loadUserAsync()

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val detail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, user)
                startActivity(detail)
            }

        })

        btn_back.setOnClickListener { finish() }
    }

    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUser = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val users = deferredUser.await()
            if (users.size > 0) {
                empty_favorite.visibility = View.INVISIBLE
                userAdapter.setUser(users)
            } else {
                rv_favorite.visibility = View.INVISIBLE
                empty_favorite.visibility = View.VISIBLE
            }
        }
    }
}