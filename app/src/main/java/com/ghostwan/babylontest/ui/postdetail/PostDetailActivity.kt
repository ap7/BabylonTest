package com.ghostwan.babylontest.ui.postdetail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ghostwan.babylontest.R
import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import com.ghostwan.babylontest.data.source.remote.PostsRemoteRepository
import com.ghostwan.babylontest.ui.util.handleError
import kotlinx.android.synthetic.main.activity_post_detail.*
import org.koin.android.ext.android.inject

class PostDetailActivity : AppCompatActivity(), PostDetailContract.View {

    companion object {
        const val EXTRA_POST_ID = "EXTRA_POST_ID"
    }

    private val repository: PostsRemoteRepository by inject()
    private val presenter: PostDetailContract.Presenter by lazy { PostDetailPresenter(repository, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        swipeView.setOnRefreshListener {
            presenter.loadPostInfo(getPostID())
        }
    }

    private fun getPostID(): Int {
        return intent.getIntExtra(EXTRA_POST_ID, -1);
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe(getPostID())
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun showPostInfo(post: Post) {
        postTitle.text = post.title
        postBody.text = post.body
    }

    override fun showComments(comments: List<Comment>) {
        commentCount.text = "${comments.size}"
    }

    override fun showUserInfo(user: User) {
        username.text = user.username
    }

    override fun showLoadingIndicator() {
        swipeView.post { swipeView.isRefreshing = true }
    }

    override fun hideLoadingIndicator() {
        swipeView.post{ swipeView.isRefreshing = false }
    }

    override fun showError(throwable: Throwable) {
        handleError(applicationContext, swipeView, throwable)
    }

}
