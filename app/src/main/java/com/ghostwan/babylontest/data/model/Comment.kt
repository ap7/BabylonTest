package com.ghostwan.babylontest.data.model

import com.google.gson.annotations.SerializedName

data class Comment (@SerializedName("id")       val id : Int,
                    @SerializedName("postId")   val postId : Int)