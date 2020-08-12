package com.fynzero.finalsubmission.view.activity

import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.adapter.ViewPagerAdapter
import com.fynzero.finalsubmission.db.DatabaseContract
import com.fynzero.finalsubmission.db.DatabaseContract.UserColumn.Companion.CONTENT_URI
import com.fynzero.finalsubmission.db.UserHelper
import com.fynzero.finalsubmission.model.User
import com.fynzero.finalsubmission.viewmodel.UserDetailViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_DETAIL = "extra_detail"
    }

    private lateinit var userDetailViewModel: UserDetailViewModel
    private lateinit var userHelper: UserHelper
    private lateinit var uriWithId: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val login = intent.getParcelableExtra<User>(EXTRA_DETAIL)
        val id = login?.id
        val username = login?.login.toString()
        val type = login?.type.toString()
        val avatar = login?.avatar_url.toString()

        // setup view pager
        val viewPagerAdapter = ViewPagerAdapter(this, supportFragmentManager)
        viewPagerAdapter.username = login?.login
        viewPager.adapter = viewPagerAdapter
        tabs.setupWithViewPager(viewPager)

        userDetailViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(UserDetailViewModel::class.java)

        userDetailViewModel.setDetail(username)
        userDetailViewModel.getDetail().observe(this, Observer { user ->
            if (user != null) {
                Picasso.get().load(user.avatar_url).placeholder(R.drawable.user).into(img_avatar)
                txt_name.text = user.name
                txt_username.text = user.login
                txt_location.text = user.location
                txt_blog.text = user.blog
                progressDetail.visibility = View.INVISIBLE
            }
        })

        btn_back.setOnClickListener { finish() }

        // set user helper
        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()

        // set favorite user
        var statusFavorite = false
        setStatusFavorite(statusFavorite)
        btn_favorite.setOnClickListener {
            if (!statusFavorite) {
                val values = ContentValues()
                values.put(DatabaseContract.UserColumn._ID, id)
                values.put(DatabaseContract.UserColumn.USERNAME, username)
                values.put(DatabaseContract.UserColumn.TYPE, type)
                values.put(DatabaseContract.UserColumn.AVATAR, avatar)
                contentResolver.insert(CONTENT_URI, values)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
                Toast.makeText(this, "Successfully added to favorites", Toast.LENGTH_SHORT).show()
            } else {
                uriWithId = Uri.parse("$CONTENT_URI/$id")
                contentResolver.delete(uriWithId, null, null)
                statusFavorite = !statusFavorite
                setStatusFavorite(statusFavorite)
                Toast.makeText(this, "Successfully removed from favorites", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // db state
        val cursor: Cursor = userHelper.queryById(id.toString())
        if (cursor.moveToNext()) {
            statusFavorite = true
            setStatusFavorite(statusFavorite)
        }
    }

    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            btn_favorite.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }
}