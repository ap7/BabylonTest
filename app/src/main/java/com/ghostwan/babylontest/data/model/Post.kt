package com.ghostwan.babylontest.data.model

import com.google.gson.annotations.SerializedName

data class Post(@SerializedName("id")       val id: Int,
                @SerializedName("userId")   val user: Int,
                @SerializedName("title")    val title: String,
                @SerializedName("body")     val body: String)