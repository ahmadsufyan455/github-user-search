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

class UserDetailViewModel : ViewModel() {
    companion object {
        private val TAG = UserDetailViewModel::class.java.simpleName
    }

    private val user = MutableLiveData<User>()
    private val noConnection = MutableLiveData<String>()

    fun setDetail(username: String) {
        ApiClient.instance.getDetail(username).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d(TAG, t.message.toString())
                noConnection.value = "Check your connection"
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                val result = response.body()
                user.postValue(result)
            }

        })
    }

    fun getDetail(): LiveData<User> {
        return user
    }

    fun noConnection(): LiveData<String> {
        return noConnection
    }
}