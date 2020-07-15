package com.yurry.githubuserfinder

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.yurry.githubuserfinder.model.GithubUser
import com.yurry.githubuserfinder.presenter.GithubUserPresenter
import com.yurry.githubuserfinder.presenter.GithubUserPresenterImpl
import com.yurry.githubuserfinder.view.GithubUserListAdapter
import com.yurry.githubuserfinder.view.GithubUserListView
import com.yurry.githubuserfinder.view.RecyclerViewLoadMoreScroll
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GithubUserListView {
    private val presenter: GithubUserPresenter = GithubUserPresenterImpl(this)
    private val adapter: GithubUserListAdapter by lazy {
        GithubUserListAdapter()
    }
    private val scrollListener: RecyclerViewLoadMoreScroll by lazy {
        val linearLayoutManager =
            LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        recycler_view.layoutManager = linearLayoutManager
        RecyclerViewLoadMoreScroll(linearLayoutManager)
    }

    private val queryHint : String by lazy { resources.getString(R.string.query_hint) }
    private val emptyMsg : String by lazy { resources.getString(R.string.response_empty) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSearchView()
        setRecyclerView()
    }

    private fun setSearchView(){
        github_user_search_bar.queryHint = queryHint
        github_user_search_bar.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {
                loading_layout.visibility = View.VISIBLE
                presenter.queryUser(query)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                //do nothing
                return false
            }
        })
    }

    private fun setRecyclerView(){
        recycler_view.adapter = adapter
        recycler_view.setHasFixedSize(true)
        scrollListener.setOnLoadMoreListener(object :
            RecyclerViewLoadMoreScroll.OnLoadMoreListener {
            override fun onLoadMore(
                currentPage: Int,
                totalItemCount: Int,
                recyclerView: RecyclerView
            ) {
                adapter.addLoadingView()
                presenter.queryMoreUser(currentPage)
            }

        })
        recycler_view.addOnScrollListener(scrollListener)

        val dividerItemDecoration = DividerItemDecoration(
            recycler_view.context,
            LinearLayoutManager.VERTICAL
        )
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(recycler_view.context, R.drawable.divider)!!)
        recycler_view.addItemDecoration(dividerItemDecoration)
    }

    override fun hideLoading() {
        loading_layout.visibility = View.GONE
    }

    override fun hideAdapterLoading() {
        adapter.removeLoadingView()
    }

    override fun setData(userList: List<GithubUser>) {
        recycler_view.smoothScrollToPosition(0)
        adapter.setData(userList)
        no_data_view.visibility = View.GONE
    }

    override fun addData(userList: List<GithubUser>) {
        Handler().postDelayed({
            adapter.removeLoadingView()
            adapter.addData(userList)
            recycler_view.post {
                adapter.notifyDataSetChanged()
            }
        }, 1000)
    }

    override fun showErrorToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    override fun showEmptyData() {
        Toast.makeText(this, emptyMsg, Toast.LENGTH_SHORT).show()
    }
}
