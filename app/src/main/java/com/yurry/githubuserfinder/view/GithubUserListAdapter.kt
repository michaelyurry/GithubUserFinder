package com.yurry.githubuserfinder.view

import android.content.res.Resources
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yurry.githubuserfinder.Constant
import com.yurry.githubuserfinder.R
import com.yurry.githubuserfinder.model.GithubUser
import kotlinx.android.synthetic.main.github_user_item.view.*

class GithubUserListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var userList: MutableList<GithubUser?> = ArrayList()
    private lateinit var resource: Resources
    private var isLoadingShown = false
    private val picasso = Picasso.get()

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        resource = parent.context.resources

        return if (viewType == Constant.VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.github_user_item, parent, false)
            ItemViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.progress_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder.itemViewType == Constant.VIEW_TYPE_ITEM) {
            val githubUser: GithubUser = getItem(position)!!

            picasso.load(githubUser.avatarUrl)
                .into(holder.itemView.github_user_picture)
            holder.itemView.github_user_name.text = githubUser.login
        }
    }

    private fun getItem(index: Int): GithubUser? {
        return userList[index]
    }

    fun setData(users: List<GithubUser>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun addData(users: List<GithubUser>) {
        userList.addAll(users)
        notifyDataSetChanged()
    }

    fun addLoadingView() {
        if (!isLoadingShown) {
            Handler().post {
                userList.add(null)
                notifyItemInserted(userList.size - 1)
            }
            isLoadingShown = true
        }
    }

    fun removeLoadingView() {
        if (isLoadingShown) {
            if (userList.size != 0) {
                userList.removeAt(userList.size - 1)
                notifyItemRemoved(userList.size)
            }
            isLoadingShown = false
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (userList[position] == null) {
            Constant.VIEW_TYPE_LOADING
        } else {
            Constant.VIEW_TYPE_ITEM
        }
    }
}