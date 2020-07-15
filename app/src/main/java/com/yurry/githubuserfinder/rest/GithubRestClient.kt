package com.yurry.githubuserfinder.rest

import com.yurry.githubuserfinder.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GithubRestClient {
    private fun getClient(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
    }

    fun getService(): GithubApi = getClient().create(GithubApi::class.java)

    fun createAuthBearer(): String {
        return Constant.PARAM_AUTH_BEARER + " " + Constant.TOKEN
    }

}