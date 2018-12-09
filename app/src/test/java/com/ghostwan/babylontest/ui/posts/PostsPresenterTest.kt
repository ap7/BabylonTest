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
import org.junit.Assert.*

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
    fun `load post list when subscribe`(){
        //Create an empty list
        every { repository.getPosts() } returns Single.just(listOf())

        //Load subscribe and load the post
        presenter.subscribe()

        verify {
            view.showLoadingIndicator()
        }
    }

    @Test
    fun `display empty list indicator when post list is empty`() {
        //Create an empty list
        every { repository.getPosts() } returns Single.just(listOf())

        //Load the posts from the repository
        presenter.loadPosts()

        //Check if the loading indicator is displayed,
        // if the empty list is displayed,
        // then if the indicator is hide
        verifySequence {
            view.showLoadingIndicator()
            view.showEmptyList()
            view.hideLoadingIndicator()
        }

    }

    @Test
    fun `display post list when post are available`() {
        //Create a list with a post
        val mockPosts = listOf(mockPost)
        every { repository.getPosts() } returns Single.just(mockPosts)

        //Load the posts from the repository
        presenter.loadPosts()

        val capturedList = slot<List<Post>>()

        //Check if the loading indicator is displayed,
        // if the post is displayed,
        // then if the indicator is hide
        verifySequence {
            view.showLoadingIndicator()
            view.showPosts(capture(capturedList))
            view.hideLoadingIndicator()
        }

        //Test if capture post is coherent
        assertTrue(capturedList.captured.isNotEmpty())
        val capturedPost = capturedList.captured[0]
        assertEquals(capturedPost.id , mockPost.id)
        assertEquals(capturedPost.user , mockPost.user)
        assertEquals(capturedPost.title , mockPost.title)
        assertEquals(capturedPost.body, mockPost.body)
    }


    @Test
    fun `display error when exception occurs`() {
        //Return a single with an exception
        val exception = Exception()
        every { repository.getPosts() } returns Single.error(exception)

        //Load the posts from the repository
        presenter.loadPosts()

        //Check if the loading indicator is displayed,
        // if the empty list is displayed and the error message
        // then if the indicator is hide
        verifySequence {
            view.showLoadingIndicator()
            view.showEmptyList()
            view.showError(exception)
            view.hideLoadingIndicator()
        }
    }

    @Test
    fun `show post detail when clicking on post`() {
        //Open post detail
        presenter.openPostDetails(mockPost)

        val capturedPost = slot<Post>()

        verify {
            view.showPostDetail(capture(capturedPost))
        }
        //Test if capture post is coherent
        assertEquals(capturedPost.captured.id, mockPost.id)
    }

    @After
    fun clean(){
        unmockkAll()
    }
}
