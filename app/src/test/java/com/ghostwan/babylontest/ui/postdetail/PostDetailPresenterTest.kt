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
import org.junit.Assert.*

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
    fun `load post info when subscribe`(){
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
    fun `display post info when post exist`(){
        //Create a mock repository
        val (post, user, comment) = createMockRepository()

        //load post information
        presenter.loadPostInfo(1)

        val capturedPost = slot<Post>()
        val capturedUser = slot<User>()
        val capturedComments = slot<List<Comment>>()

        //Verify if the loading indicator is displayed
        verifyAll {
            view.showLoadingIndicator()
            view.showPostInfo(capture(capturedPost))
            view.showUserInfo(capture(capturedUser))
            view.showComments(capture(capturedComments))
            view.hideLoadingIndicator()
        }

        //Verify that no error are displayed
        verify(inverse = true) {
            view.showError(any())
        }

        //Test if captured post is coherent
        assertEquals(capturedPost.captured.id, post.id)
        assertEquals(capturedPost.captured.title, post.title)
        assertEquals(capturedPost.captured.body, post.body)
        assertEquals(capturedPost.captured.user, post.user)

        //Test if one comment was retrieved
        assertTrue(capturedComments.captured.size == 1)

        //Test if captured user ID is the same than post user ID
        assertEquals(capturedUser.captured.id, post.user)

        //Test user data
        assertEquals(capturedUser.captured.username, user.username)

        //Test comment data
        assertEquals(capturedComments.captured[0].name, comment.name)
    }

    @Test
    fun `display error when exception occurs`() {
        //Return a single with an exception
        val exception = Exception()
        every { repository.getPost(any()) } returns Single.error(exception)
        every { repository.getPostComments(any()) } returns Single.error(exception)

        //load post information
        presenter.loadPostInfo(1)

        //Verify that an error message is displayed
        verifySequence {
            view.showLoadingIndicator()
            view.showError(exception)
            view.hideLoadingIndicator()
        }

    }

    @After
    fun clean(){
        unmockkAll()
    }
}