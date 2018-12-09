package com.ghostwan.babylontest.ui.posts

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ghostwan.babylontest.R
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.source.remote.PostsRemoteRepository
import com.ghostwan.babylontest.ui.postdetail.PostDetailActivity
import com.ghostwan.babylontest.ui.util.handleError
import kotlinx.android.synthetic.main.activity_list_post.*
import org.koin.android.ext.android.inject


class PostsActivity : AppCompatActivity(), PostsContract.View {

    private val repository: PostsRemoteRepository by inject()
    private val presenter: PostsContract.Presenter by lazy { PostsPresenter(repository, this) }
    private val viewManager: RecyclerView.LayoutManager by lazy { LinearLayoutManager(this) }
    private val viewAdapter: PostAdapter by lazy { PostAdapter(presenter::openPostDetails) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_post)

        postsList.apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter
        }

        swipeView.setOnRefreshListener {
            presenter.loadPosts()
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unsubscribe()
    }

    override fun showLoadingIndicator() {
        swipeView.post { swipeView.isRefreshing = true }
    }

    override fun hideLoadingIndicator() {
        swipeView.post{ swipeView.isRefreshing = false }
    }

    override fun showPosts(posts: List<Post>) {
        emptyList.visibility = View.GONE
        postsList.visibility = View.VISIBLE
        viewAdapter.submitList(posts)
    }

    override fun showEmptyList() {
        postsList.visibility = View.GONE
        emptyList.visibility = View.VISIBLE
    }

    override fun showPostDetail(post: Post) {
        val intent = Intent(this, PostDetailActivity::class.java)
        intent.putExtra(PostDetailActivity.EXTRA_POST_ID, post.id)
        startActivity(intent)
    }

    override fun showError(throwable: Throwable) {
        handleError(applicationContext, swipeView, throwable)
    }

}
