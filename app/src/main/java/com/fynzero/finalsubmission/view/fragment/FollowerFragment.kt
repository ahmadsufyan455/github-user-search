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
import com.fynzero.finalsubmission.viewmodel.FollowerViewModel
import kotlinx.android.synthetic.main.fragment_follower.*

class FollowerFragment : Fragment() {
    companion object {
        private const val ARG_USERNAME = "username"

        fun getUsername(username: String): FollowerFragment {
            val fragment = FollowerFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    private val userList = ArrayList<User>()
    private lateinit var followerViewModel: FollowerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follower, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val username = arguments?.getString(ARG_USERNAME).toString()

        // setup recycler view
        rv_follower.layoutManager = LinearLayoutManager(activity)
        rv_follower.setHasFixedSize(true)

        val userAdapter = UserAdapter(userList)
        userAdapter.notifyDataSetChanged()
        rv_follower.adapter = userAdapter

        followerViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowerViewModel::class.java)

        followerViewModel.setFollower(username)
        followerViewModel.getFollower().observe(activity!!, Observer { users ->
            if ((users != null) && (users.size != 0)) {
                userAdapter.setUser(users)
            } else {
                txt_follower.text = username + resources.getString(R.string.no_follower)
                rv_follower.visibility = View.INVISIBLE
                no_follower.visibility = View.VISIBLE
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