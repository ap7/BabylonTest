package com.ghostwan.babylontest.ui.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ghostwan.babylontest.R
import com.ghostwan.babylontest.data.model.Post
import kotlinx.android.synthetic.main.post_row.view.*

class PostAdapter(private val postListener: (Post) -> Unit) : ListAdapter<Post, PostAdapter.Holder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.post_row, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position), postListener)
    }

    class Holder(row: View): RecyclerView.ViewHolder(row) {
        fun bind(post: Post, clickListener: (Post) -> Unit) {
            itemView.postTitle.text = post.title
            itemView.setOnClickListener { clickListener(post) }
        }
    }
}