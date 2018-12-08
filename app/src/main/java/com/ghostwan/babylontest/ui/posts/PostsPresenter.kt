package com.ghostwan.babylontest.ui.posts

import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.source.PostsDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostsPresenter(private val repository: PostsDataSource, private val view: PostsContract.View) : PostsContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun openPostDetails(post: Post) {
        view.showPostDetail(post)
    }

    override fun loadPosts() {
        compositeDisposable.clear()
        view.showLoadingIndicator()
        val disposable = repository.getPosts()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {view.hideLoadingIndicator()}
            .subscribe({
                if(it.isEmpty())
                    view.showEmptyList()
                else
                    view.showPosts(it)
            }, {view.showError(it)})
        compositeDisposable.add(disposable)
    }

    override fun subscribe() {
        loadPosts()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

}

