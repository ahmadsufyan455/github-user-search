package com.fynzero.finalsubmission.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.fynzero.finalsubmission.R
import com.fynzero.finalsubmission.adapter.UserAdapter
import com.fynzero.finalsubmission.model.User
import com.fynzero.finalsubmission.view.activity.DetailActivity
import com.fynzero.finalsubmission.viewmodel.FollowingViewModel
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {
    companion object {
        private const val ARG_USERNAME = "username"

        fun getUsername(username: String): FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var followingViewModel: FollowingViewModel
    private val userList = ArrayList<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME).toString()

        // setup recycle view
        rv_following.layoutManager = LinearLayoutManager(activity)
        rv_following.setHasFixedSize(true)

        val userAdapter = UserAdapter(userList)
        userAdapter.notifyDataSetChanged()
        rv_following.adapter = userAdapter

        followingViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowingViewModel::class.java)

        followingViewModel.setFollowing(username)
        followingViewModel.getFollowing().observe(activity!!, Observer { users ->
            if ((users != null) && (users.size != 0)) {
                userAdapter.setUser(users)
            } else {
                txt_following.text = username + resources.getString(R.string.no_following)
                rv_following.visibility = View.INVISIBLE
                no_following.visibility = View.VISIBLE
            }
        })

        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(user: User) {
                val detail = Intent(activity, DetailActivity::class.java)
                detail.putExtra(DetailActivity.EXTRA_DETAIL, user)
                startActivity(detail)
            }

        })
    }
}