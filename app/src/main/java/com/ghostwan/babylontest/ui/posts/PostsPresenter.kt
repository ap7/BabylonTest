package com.ghostwan.babylontest.ui.posts

import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.source.PostsDataSource
import com.ghostwan.babylontest.ui.util.BaseSchedulerProvider
import com.ghostwan.babylontest.ui.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class PostsPresenter(private val repository: PostsDataSource,
                     private val view: PostsContract.View,
                     private val scheduler: BaseSchedulerProvider = SchedulerProvider()) : PostsContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun openPostDetails(post: Post) {
        view.showPostDetail(post)
    }

    override fun loadPosts() {
        compositeDisposable.clear()
        view.showLoadingIndicator()
        val disposable = repository.getPosts()
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doFinally {view.hideLoadingIndicator()}
            .subscribe({
                if(it.isEmpty())
                    view.showEmptyList()
                else
                    view.showPosts(it)
            }, {
                view.showEmptyList()
                view.showError(it)
            })
        compositeDisposable.add(disposable)
    }

    override fun subscribe() {
        loadPosts()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }

}

