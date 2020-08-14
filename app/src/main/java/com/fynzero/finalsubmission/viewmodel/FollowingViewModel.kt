package com.fynzero.finalsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fynzero.finalsubmission.model.User
import com.fynzero.finalsubmission.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingViewModel : ViewModel() {
    companion object {
        private val TAG = FollowingViewModel::class.java.simpleName
    }

    private val userList = MutableLiveData<ArrayList<User>>()
    private val noConnection = MutableLiveData<String>()

    fun setFollowing(username: String) {
        ApiClient.instance.getFollowing(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                noConnection.value = "Check your connection"
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                val result = response.body()
                userList.postValue(result)
            }

        })
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return userList
    }

    fun noConnection(): LiveData<String> {
        return noConnection
    }
}