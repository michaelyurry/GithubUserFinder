package com.yurry.githubuserfinder.model

import com.google.gson.annotations.SerializedName

data class GithubUserSearchResponse(
    @SerializedName("total_count") val totalCount : Int,
    @SerializedName("incomplete_results") val incompleteResult: Boolean,
    @SerializedName("items") val items: List<GithubUser>
)