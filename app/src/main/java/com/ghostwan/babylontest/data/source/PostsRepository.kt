package com.ghostwan.babylontest.data.source

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import io.reactivex.Single

class PostsRepository(private val remote: PostsDataSource) : PostsDataSource {

    override fun getPost(id: Int): Single<Post> {
        return remote.getPost(id)
    }

    override fun getPostComments(id: Int): Single<List<Comment>> {
        return remote.getPostComments(id)
    }

    override fun getPosts(): Single<List<Post>> {
        return remote.getPosts()
    }

    override fun getUser(id: Int): Single<User> {
        return remote.getUser(id)
    }
}
