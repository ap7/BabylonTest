package com.ghostwan.babylontest.data.source.remote

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import com.ghostwan.babylontest.data.source.PostsDataSource
import com.ghostwan.babylontest.data.source.remote.service.PostService
import io.reactivex.Single
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class PostsRemoteRepository(baseUrl: String) : PostsDataSource {

    private val retrofit = lazy { Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    }
    private val postsService = lazy {
        retrofit.value.create<PostService>(PostService::class.java)
    }

    override fun getPosts(): Single<List<Post>> {
        return postsService.value.getPosts()
    }

    override fun getPost(id: Int): Single<Post> {
        return postsService.value.getPost(id)
    }

    override fun getPostComments(id: Int): Single<List<Comment>> {
        return postsService.value.getCommentsForPost(id)
    }

    override fun getUser(id: Int): Single<User> {
        return postsService.value.getUser(id)
    }

}