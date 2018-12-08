package com.ghostwan.babylontest.data.source.remote.service

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PostService {

    @GET("posts/")
    fun getPosts(): Single<List<Post>>

    @GET("posts/{postId}")
    fun getPost(@Path("postId") postId: Int): Single<Post>

    @GET("users/{userId}")
    fun getUser(@Path("userId") userId:Int): Single<User>

    @GET("comments")
    fun getCommentsForPost(@Query("postId") postId:Int): Single<List<Comment>>

}