package com.demo.simpleurbandictionary.model

import com.google.gson.annotations.SerializedName

/**
 * Created on 2019-05-12.
 *
 * @author kumars
 */
data class Search(val definition: String, val example: String, @SerializedName("thumbs_up") val thumbsUp: Int, @SerializedName("thumbs_down") val thumbDown: Int)