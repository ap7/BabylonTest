package com.ghostwan.babylontest.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.ghostwan.babylontest.R
import com.ghostwan.babylontest.data.source.MockPostsRepository
import com.ghostwan.babylontest.data.source.PostsRepository
import com.ghostwan.babylontest.ui.posts.PostsActivity
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class PostActivitiesTest {

    val mockPostsRepository = MockPostsRepository()

    @Rule
    @JvmField
    val rule = object : ActivityTestRule<PostsActivity>(PostsActivity::class.java) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            val mockRepositoryModule = module {
                single(override=true) { PostsRepository(mockPostsRepository) }
            }
            loadKoinModules(listOf(mockRepositoryModule))
        }
    }
    val postsScreen = PostsScreen()
    val postDetailScreen = PostDetailScreen()

    @Test
    fun isListDisplayed_when_connectionOK() {
        mockPostsRepository.isConnected = true
        postsScreen {
            list {
                firstChild<PostsScreen.Item> {
                    isVisible()
                    title { hasText(mockPostsRepository.posts[0].title) }
                }
            }
            emptyList { isGone()}
        }
    }

    @Test
    fun isListDisplayed_after_refreshing() {
        mockPostsRepository.isConnected = true
        postsScreen {
            swipeView{
                swipeDown()
            }
            list {
                firstChild<PostsScreen.Item> {
                    isVisible()
                    title { hasText(mockPostsRepository.posts[0].title) }
                }
            }
            emptyList { isGone() }
        }
    }

    @Test
    fun isEmptyListDisplayed_when_connectionKO() {
        mockPostsRepository.isConnected = false
        postsScreen {
            swipeView{
                swipeDown()
            }
            list { isGone() }
            emptyList { isVisible() }
            errorMessage {
                isVisible()
                text {hasText(R.string.network_error) }
            }
        }
    }

    @Test
    fun isDetailDisplayedAreCorrect_when_clickingOnPost() {
        mockPostsRepository.isConnected = true
        postsScreen {
            list {
                firstChild<PostsScreen.Item> {
                    click()
                }
            }
        }
        postDetailScreen {
            title { hasText(mockPostsRepository.posts[0].title) }
            body { hasText(mockPostsRepository.posts[0].body) }
            username { hasText(mockPostsRepository.users[0].username) }
            commentCount { hasText("${mockPostsRepository.comments[1]?.size}") }
        }
    }

    @Test
    fun isErrorMessageDisplayed_when_clickingOnPost_and_connectionKO() {
        mockPostsRepository.isConnected = false
        postsScreen {
            list {
                firstChild<PostsScreen.Item> {
                    click()
                }
            }
        }
        postDetailScreen {
            title { hasText("") }
            body { hasText("") }
            username { hasText("") }
            commentCount { hasText("0") }

            errorMessage {
                isVisible()
                text {hasText(R.string.network_error) }
            }
        }
    }
}

