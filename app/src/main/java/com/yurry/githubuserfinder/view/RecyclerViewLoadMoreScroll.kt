package com.yurry.githubuserfinder.view

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewLoadMoreScroll(layoutManager: LinearLayoutManager) :
    RecyclerView.OnScrollListener() {

    private var visibleThreshold = 5
    private lateinit var mOnLoadMoreListener: OnLoadMoreListener
    private var currentPage = 1
    private val startingPageIndex = 1

    private var isLoading: Boolean = false
    private var lastVisibleItem: Int = 0
    private var totalItemCount:Int = 0
    private var previousTotalItemCount: Int = 0

    private var mLayoutManager: RecyclerView.LayoutManager = layoutManager


    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener
    }

    interface OnLoadMoreListener {
        fun onLoadMore(currentPage: Int, totalItemCount: Int, recyclerView: RecyclerView)
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        if (dy <= 0) return

        totalItemCount = mLayoutManager.itemCount

        lastVisibleItem = (mLayoutManager as LinearLayoutManager).findLastVisibleItemPosition()

        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex
            previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                isLoading = true
            }
        }

        if (isLoading && totalItemCount > previousTotalItemCount) {
            isLoading = false
            previousTotalItemCount = totalItemCount
        }

        if (!isLoading && lastVisibleItem + visibleThreshold > totalItemCount) {
            currentPage++
            mOnLoadMoreListener.onLoadMore(currentPage, totalItemCount, recyclerView)
            isLoading = true
        }

    }

    fun resetState() {
        currentPage = startingPageIndex
        previousTotalItemCount = 0
    }
}