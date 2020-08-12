package com.fynzero.finalsubmission.network

import com.fynzero.finalsubmission.model.User
import com.fynzero.finalsubmission.model.UserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiEndpoint {
    @GET("search/users")
    @Headers("Authorization: token 3541a6a941ab3208817df38e63a1921ef5cf6452")
    fun getUser(@Query("q") username: String): Call<UserResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 3541a6a941ab3208817df38e63a1921ef5cf6452")
    fun getDetail(@Path("username") username: String): Call<User>

    @GET("users/{username}/following")
    @Headers("Authorization: token 3541a6a941ab3208817df38e63a1921ef5cf6452")
    fun getFollowing(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 3541a6a941ab3208817df38e63a1921ef5cf6452")
    fun getFollower(@Path("username") username: String): Call<ArrayList<User>>
}