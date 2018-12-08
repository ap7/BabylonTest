package com.ghostwan.babylontest.data.source

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import io.reactivex.Single


interface PostsDataSource {

    fun getPosts(): Single<List<Post>>

    fun getPost(id: Int): Single<Post>

    fun getPostComments(id: Int): Single<List<Comment>>

    fun getUser(id: Int):Single<User>
}