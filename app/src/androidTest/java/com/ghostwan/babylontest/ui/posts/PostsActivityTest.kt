package com.ghostwan.babylontest.ui.posts

import android.view.View
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.agoda.kakao.*
import com.ghostwan.babylontest.R
import com.ghostwan.babylontest.data.source.MockPostsRepository
import com.ghostwan.babylontest.data.source.PostsRepository
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

@RunWith(AndroidJUnit4::class)
class PostsActivityTest {

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


    class PostsScreen : Screen<PostsScreen>() {
        val list  = KRecyclerView({
            withId(R.id.postsList)
        }, itemTypeBuilder = {
            itemType(::Item)
        })
        val swipeView =  KSwipeView {
            withId(R.id.swipeView)
        }

        val emptyList = KView {
            withId(R.id.emptyList)
        }

        val errorMessage = KSnackbar()

        class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
            val title: KTextView = KTextView(parent) { withId(R.id.postTitle) }
        }
    }


    val screen = PostsScreen()

    @Test
    fun isListDisplayed_when_connectionOK() {
        mockPostsRepository.isConnected = true
        screen {
            list {
                firstChild<PostsScreen.Item> {
                    isVisible()
                    title { hasText("post1") }
                }
            }
        }
    }

    @Test
    fun isListDisplayed_after_refreshing() {
        mockPostsRepository.isConnected = true
        screen {
            swipeView{
                swipeDown()
            }
            list {
                firstChild<PostsScreen.Item> {
                    isVisible()
                    title { hasText("post1") }
                }
            }
        }
    }

    @Test
    fun isEmptyListDisplayed_when_connectionKO() {
        mockPostsRepository.isConnected = false
        screen {
            swipeView{
                swipeDown()
            }
            list { isGone() }
            emptyList { isVisible() }
            errorMessage { isVisible() }
        }
    }
}

