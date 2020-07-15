package com.yurry.githubuserfinder.presenter

import com.yurry.githubuserfinder.model.GithubUserSearchResponse
import com.yurry.githubuserfinder.rest.GithubRestClient
import com.yurry.githubuserfinder.view.GithubUserListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUserPresenterImpl(private val view: GithubUserListView) : GithubUserPresenter{
    private val bearer = GithubRestClient().createAuthBearer()
    private lateinit var lastQuery: String
    override fun queryUser(query: String) {
        lastQuery = query
        GithubRestClient().getService().getUser(bearer, query, 1, 20)
            .enqueue(object : Callback<GithubUserSearchResponse>{
                override fun onFailure(call: Call<GithubUserSearchResponse>, t: Throwable) {
                    view.showErrorToast("Error: ${t.message}")
                    view.hideLoading()
                }

                override fun onResponse(
                    call: Call<GithubUserSearchResponse>,
                    response: Response<GithubUserSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (response.body()!!.items.isEmpty()) {
                                view.showEmptyData()
                            } else{
                                view.setData(response.body()!!.items)
                            }
                            view.hideLoading()
                        }
                    } else {
                        response.code()
                        view.showErrorToast("Error: ${response.code()}")
                        view.hideLoading()
                    }
                }
            })
    }

    override fun queryMoreUser(page: Int) {
        GithubRestClient().getService().getUser(bearer, lastQuery, page, 20)
            .enqueue(object : Callback<GithubUserSearchResponse>{
                override fun onFailure(call: Call<GithubUserSearchResponse>, t: Throwable) {
                    view.showErrorToast("Error: ${t.message}")
                    view.hideAdapterLoading()
                }

                override fun onResponse(
                    call: Call<GithubUserSearchResponse>,
                    response: Response<GithubUserSearchResponse>
                ) {
                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            if (response.body()!!.items.isEmpty()) {
                                view.hideAdapterLoading()
                            } else{
                                view.addData(response.body()!!.items)
                            }
                        }
                    } else {
                        response.code()
                        view.showErrorToast("Error: ${response.code()}")
                        view.hideAdapterLoading()
                    }
                }
            })
    }

}