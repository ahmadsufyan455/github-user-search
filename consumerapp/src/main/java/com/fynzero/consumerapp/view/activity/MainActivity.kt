package com.fynzero.consumerapp.view.activity

import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.consumerapp.R
import com.fynzero.consumerapp.adapter.UserAdapter
import com.fynzero.consumerapp.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.fynzero.consumerapp.helper.MappingHelper
import com.fynzero.consumerapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val userList = ArrayList<User>()
    private val userAdapter = UserAdapter(userList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
