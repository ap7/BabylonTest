package com.ghostwan.babylontest.ui

import android.view.View
import com.agoda.kakao.*
import com.ghostwan.babylontest.R
import org.hamcrest.Matcher

class PostsScreen : Screen<PostsScreen>() {
    val list  = KRecyclerView({
        withId(R.id.postsList)
    }, itemTypeBuilder = {
        itemType(::Item)
    })
    val swipeView = KSwipeView {
        withId(R.id.swipeView)
    }

    val emptyList = KView {
        withId(R.id.emptyList)
    }

    val errorMessage = KSnackbar()

    class Item(parent: Matcher<View>) : KRecyclerItem<Item>(parent) {
        val title: KTextView =
            KTextView(parent) { withId(R.id.postTitle) }
    }
}