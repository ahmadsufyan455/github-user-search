package com.fynzero.finalsubmission.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fynzero.finalsubmission.model.User
import com.fynzero.finalsubmission.model.UserResponse
import com.fynzero.finalsubmission.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserSearchViewModel : ViewModel() {
    companion object {
        private val TAG = UserSearchViewModel::class.java.simpleName
    }

    private val userList = MutableLiveData<ArrayList<User>>()
    private val noConnection = MutableLiveData<String>()

    fun setUser(username: String) {
        ApiClient.instance.getUser(username).enqueue(object : Callback<UserResponse> {
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                noConnection.value = "Check your connection"
            }

            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                val items = response.body()?.items
                Log.d(TAG, items.toString())
                userList.postValue(items)
            }
        })
    }

    fun getUser(): LiveData<ArrayList<User>> {
        return userList
    }

    fun noConnection() : LiveData<String> {
        return noConnection
    }
}