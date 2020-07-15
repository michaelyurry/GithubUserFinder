package com.yurry.githubuserfinder.rest

import com.yurry.githubuserfinder.model.GithubUserSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query

interface GithubApi {
    @Headers("Content-Type: application/json")
    @GET("search/users")
    fun getUser(
        @Header("Authorization") authorization: String,
        @Query("q") username: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Call<GithubUserSearchResponse>
}