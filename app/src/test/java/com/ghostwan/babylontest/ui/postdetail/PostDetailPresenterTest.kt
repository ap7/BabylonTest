package com.ghostwan.babylontest.ui.postdetail

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import com.ghostwan.babylontest.data.source.PostsDataSource
import com.ghostwan.babylontest.ui.util.ImmediateSchedulerProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.junit.After
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for the implementation of {@link PostDetailPresenter}
 */
class PostDetailPresenterTest {
    @MockK
    lateinit var repository: PostsDataSource

    @MockK
    lateinit var view : PostDetailContract.View

    private lateinit var presenter: PostDetailPresenter


    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        presenter = PostDetailPresenter(repository, view, ImmediateSchedulerProvider())
    }

    //Set a mock repository
    fun createMockRepository(): Triple<Post, User, Comment> {
        val mockPost = Post(1, 1, "mocktitle1", "mockbody1")
        val mockUser = User(1, "name1", "username1")
        val mockComment = Comment(1, 1, "title1", "email1", "body1")

        every { repository.getPosts() } returns Single.just(listOf(mockPost))
        every { repository.getPost(1) } returns Single.just(mockPost)
        every { repository.getPostComments(1) } returns Single.just(listOf(mockComment))
        every { repository.getUser(1) } returns Single.just(mockUser)

        return Triple(mockPost, mockUser, mockComment)
    }

    @Test
    fun loadPostInfo_when_subscribe(){
        //Create a mock repository
        createMockRepository()

        //Subscribe and load post information
        presenter.subscribe(1)

        //Verify if the loading indicator is displayed
        verify {
            view.showLoadingIndicator()
        }
    }

    @Test
    fun displayPostInfo_when_postAvailable(){
        //Create a mock repository
        val (post, user, comment) = createMockRepository()

        //load post information
        presenter.loadPostInfo(1)

        //Verify if the loading indicator is displayed
        verifyAll {
            view.showLoadingIndicator()
            view.showPostInfo(post)
            view.showUserInfo(user)
            view.showComments(listOf(comment))
            view.hideLoadingIndicator()
        }

        //Verify that no error are displayed
        verify(inverse = true) {
            view.showError(any())
        }
    }

    @Test
    fun displayError_when_postDetailUnavailable() {
        val exception = Exception()
        every { repository.getPost(1) } returns Single.error(exception)

        //        verifyOrder {
//            view.showLoadingIndicator()
//            view.showError(exception)
//            view.hideLoadingIndicator()
//        }

        //Verify that those function are not called
        verify(inverse = true) {
            view.showPostInfo(any())
            view.showUserInfo(any())
            view.showComments(any())
        }

    }

    @After
    fun clean(){
        unmockkAll()
    }
}