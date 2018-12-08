package com.ghostwan.babylontest.ui.posts

import com.ghostwan.babylontest.data.model.Post

interface PostsContract {
    interface View {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showPosts(posts : List<Post>)
        fun showEmptyList()
        fun showPostDetail(post: Post)
        fun showError(throwable: Throwable)
    }
    interface Presenter {
        fun loadPosts()
        fun openPostDetails(post: Post)
        fun subscribe()
        fun unsubscribe()
    }
}

