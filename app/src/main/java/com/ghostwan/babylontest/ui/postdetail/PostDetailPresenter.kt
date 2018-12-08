package com.ghostwan.babylontest.ui.postdetail

import com.ghostwan.babylontest.data.source.PostsDataSource
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PostDetailPresenter(private val repository: PostsDataSource, private val view: PostDetailContract.View)  : PostDetailContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun loadPostInfo(postId : Int) {
        compositeDisposable.clear()
        view.showLoadingIndicator()
        val postDisposable = repository.getPost(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally {view.hideLoadingIndicator()}
            .doOnSuccess {
               val userDisposable = repository.getUser(it.user)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({view.showUserInfo(it)}, {view.showError(it)})
                compositeDisposable.add(userDisposable)
            }
            .subscribe({ view.showPostInfo(it)}, {view.showError(it)})

        val commentDisposable = repository.getPostComments(postId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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