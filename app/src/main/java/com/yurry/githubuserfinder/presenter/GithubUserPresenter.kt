package com.yurry.githubuserfinder.presenter

interface GithubUserPresenter {
    fun queryUser(query: String)
    fun queryMoreUser(page: Int)
}