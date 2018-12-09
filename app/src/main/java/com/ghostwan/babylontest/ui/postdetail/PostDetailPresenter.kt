package com.ghostwan.babylontest.ui.postdetail

import com.ghostwan.babylontest.data.source.PostsDataSource
import com.ghostwan.babylontest.ui.util.BaseSchedulerProvider
import com.ghostwan.babylontest.ui.util.SchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class PostDetailPresenter(private val repository: PostsDataSource,
                          private val view: PostDetailContract.View,
                          private val scheduler: BaseSchedulerProvider = SchedulerProvider())  : PostDetailContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadPostInfo(postId : Int) {
        compositeDisposable.clear()
        view.showLoadingIndicator()
        val postDisposable = repository.getPost(postId)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .doFinally {view.hideLoadingIndicator()}
            .doOnSuccess {
               val userDisposable = repository.getUser(it.user)
                    .subscribeOn(scheduler.io())
                    .observeOn(scheduler.ui())
                    .subscribe({view.showUserInfo(it)}, {view.showError(it)})
                compositeDisposable.add(userDisposable)
            }
            .subscribe({ view.showPostInfo(it)}, {view.showError(it)})

        val commentDisposable = repository.getPostComments(postId)
            .subscribeOn(scheduler.io())
            .observeOn(scheduler.ui())
            .subscribe({view.showComments(it)}, {view.showError(it)})

        compositeDisposable.add(postDisposable)
        compositeDisposable.add(commentDisposable)
    }

    override fun subscribe(postId: Int) {
        loadPostInfo(postId)
    }


    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}