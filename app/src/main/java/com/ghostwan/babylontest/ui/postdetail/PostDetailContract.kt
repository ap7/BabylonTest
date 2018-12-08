package com.ghostwan.babylontest.ui.postdetail

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User

interface PostDetailContract {
    interface View {
        fun showLoadingIndicator()
        fun hideLoadingIndicator()
        fun showPostInfo(post: Post)
        fun showUserInfo(user: User)
        fun showError(throwable: Throwable)
        fun showComments(comments: List<Comment>)
    }

    interface Presenter{
        fun loadPostInfo(postId: Int)
        fun subscribe(postId: Int)
        fun unsubscribe()
    }
}