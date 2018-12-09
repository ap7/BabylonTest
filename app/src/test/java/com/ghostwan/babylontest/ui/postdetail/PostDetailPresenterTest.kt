package com.ghostwan.babylontest.ui.postdetail

import com.ghostwan.babylontest.data.model.Comment
import com.ghostwan.babylontest.data.model.Post
import com.ghostwan.babylontest.data.model.User
import com.ghostwan.babylontest.data.source.PostsDataSource
import com.ghostwan.babylontest.ui.util.ImmediateSchedulerProvider
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.Single
import org.amshove.kluent.shouldBeEqualTo
import org.amshove.kluent.shouldEqualTo
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

    // Create a mock repository with one post, one user and one comment
    fun createMockRepository(): Triple<Post, User, Comment> {
        val mockPost = Post(1, 1, "mocktitle1", "mockbody1")
        val mockUser = User(1, "name1", "username1")
        val mockComment = Comment(1, 1)

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

        val slotPost = slot<Post>()
        val slotUser = slot<User>()
        val slotComments = slot<List<Comment>>()

        //Verify if the loading indicator is displayed
        verifyAll {
            view.showLoadingIndicator()
            view.showPostInfo(capture(slotPost))
            view.showUserInfo(capture(slotUser))
            view.showComments(capture(slotComments))
            view.hideLoadingIndicator()
        }

        //Verify that no error are displayed
        verify(inverse = true) {
            view.showError(any())
        }

        val capturedPost = slotPost.captured
        val capturedUser = slotUser.captured
        val capturedComments = slotComments.captured

        //Test if captured post is coherent
        capturedPost.id shouldEqualTo post.id
        capturedPost.title shouldBeEqualTo post.title
        capturedPost.body shouldBeEqualTo post.body
        capturedPost.user shouldEqualTo post.user

        //Test if captured user ID is the same than post user ID
        capturedUser.id shouldEqualTo post.user

        //Test user data
        capturedUser.username shouldBeEqualTo user.username

        //Test if one comment was retrieved
        capturedComments.size shouldEqualTo 1
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