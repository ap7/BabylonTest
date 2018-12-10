package com.ghostwan.babylontest.ui

import com.agoda.kakao.KSnackbar
import com.agoda.kakao.KTextView
import com.agoda.kakao.Screen
import com.ghostwan.babylontest.R

class PostDetailScreen : Screen<PostDetailScreen>() {
    val title  = KTextView {
        withId(R.id.postTitle)
    }
    val body  = KTextView {
        withId(R.id.postBody)
    }
    val username  = KTextView {
        withId(R.id.username)
    }
    val commentCount  = KTextView {
        withId(R.id.commentCount)
    }
    val errorMessage = KSnackbar()

}