package com.yurry.githubuserfinder.view

import com.yurry.githubuserfinder.model.GithubUser

interface GithubUserListView {
    fun hideLoading()
    fun hideAdapterLoading()
    fun setData(userList: List<GithubUser>)
    fun addData(userList: List<GithubUser>)
    fun showErrorToast(msg: String)
    fun showEmptyData()
}