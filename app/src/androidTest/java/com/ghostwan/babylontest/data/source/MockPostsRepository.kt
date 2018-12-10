package com.ghostwan.babylontest.data.source

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import io.reactivex.Single
import java.net.UnknownHostException

class MockPostsRepository(var isConnected: Boolean = true) : PostsDataSource {

    companion object {
        var commentCpt = 0
    }

    val posts = listOf(
        Post(1, 1, "post1", "user1 -> post1"),
        Post(2, 1, "post2", "user1 -> post2"),
        Post(3, 2, "post3", "user2 -> post3")
    )

    val users = listOf(
        User(1, "user1" , "username1"),
        User(2, "user2" , "username2")
    )

    val comments = mapOf(
        1 to createListOfComment(1, 3),
        2 to createListOfComment(2, 5),
        3 to createListOfComment(3, 7)
    )

    override fun getPosts(): Single<List<Post>> {
        if(!isConnected)
            return Single.error(UnknownHostException())

        return Single.just(posts)
    }

    override fun getPost(id: Int): Single<Post> {
        if(!isConnected)
            return Single.error(UnknownHostException())

        return if(id in 1..3)
            Single.just(posts[id-1])
        else
            Single.error(PostNotFoundException())
    }

    override fun getPostComments(id: Int): Single<List<Comment>> {
        if(!isConnected)
            return Single.error(UnknownHostException())

        return if(id in 1..3)
            Single.just(comments[id])
        else
            Single.error(PostNotFoundException())
    }

    override fun getUser(id: Int): Single<User> {
        if(!isConnected)
            return Single.error(UnknownHostException())

        return if(id in 1..2)
            Single.just(users[id-1])
        else
            Single.error(UserNotFoundException())
    }

    fun createListOfComment(postID: Int, commentCount: Int):List<Comment> {
        val list = mutableListOf<Comment>()
        for (cpt in 0..commentCount) {
            commentCpt++
            list.add(Comment(commentCpt, postID))
        }
        return list
    }

}

class PostNotFoundException : Exception()
class UserNotFoundException : Exception()
