package com.ghostwan.babylontest.ui.posts

import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.source.PostsDataSource
import com.ghostwan.babylontest.ui.util.ImmediateSchedulerProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the implementation of {@link PostsPresenter}
 */
class PostsPresenterTest {

    @MockK
    lateinit var repository: PostsDataSource

    @MockK
    lateinit var view : PostsContract.View

    private lateinit var presenter: PostsPresenter

    private var mockPost = Post(1, 1, "mocktitle1", "mockbody1")


    @Before
    fun setUp () {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = PostsPresenter(repository, view, ImmediateSchedulerProvider())
    }

    @Test
    fun loadPosts_when_subscribe(){
        //Create an empty list
        every { repository.getPosts() } returns Single.just(listOf())

        //Load subscribe and load the post
        presenter.subscribe()

        verify {
            view.showLoadingIndicator()
        }
    }

    @Test
    fun displayEmptyList_when_postListIsEmpty() {
        //Create an empty list
        every { repository.getPosts() } returns Single.just(listOf())

        //Load the posts from the repository
        presenter.loadPosts()

        //Check if the loading indicator is displayed,
        // if the empty list is displayed,
        // then if the indicator is hide
        verifyOrder {
            view.showLoadingIndicator()
            view.showEmptyList()
            view.hideLoadingIndicator()
        }

        //Verify that those methods weren't called
        verify(inverse = true) {
            view.showError(Throwable())
            view.showPosts(any())
        }

    }

    @Test
    fun displayPosts_when_postsAvailable() {
        //Create a list with a post
        val mockPosts = listOf(mockPost)
        every { repository.getPosts() } returns Single.just(mockPosts)

        //Load the posts from the repository
        presenter.loadPosts()

        //Check if the loading indicator is displayed,
        // if the post is displayed,
        // then if the indicator is hide
        verifyOrder {
            view.showLoadingIndicator()
            view.showPosts(mockPosts)
            view.hideLoadingIndicator()
        }
    }


    @Test
    fun displayError_when_exceptionHappens() {
        //Return a single with an exception
        val exception = Exception()
        every { repository.getPosts() } returns Single.error(exception)

        //Load the posts from the repository
        presenter.loadPosts()

        //Check if the loading indicator is displayed,
        // if the empty list is displayed and the error message
        // then if the indicator is hide
        verifyAll {
            view.showLoadingIndicator()
            view.showError(exception)
            view.showEmptyList()
            view.hideLoadingIndicator()
        }
    }

    @Test
    fun showPostDetail_when_clickingOnPost() {
        //Open post detail
        presenter.openPostDetails(mockPost)

        verify {
            view.showPostDetail(mockPost)
        }
    }

    @After
    fun clean(){
        unmockkAll()
    }
}
