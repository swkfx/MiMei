package com.fangx.mimei.data.server

import com.google.gson.annotations.SerializedName

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/11
 *      desc   :
 * </pre>
 */
data class Mei(val title: String,
               val imgUrl: String,
               val author: String,
               val created_at: String,
               val publishedAt: String,
               val updated_at: String,
               val _id: String,
               val content: String,
               val rand_id: String
)

data class MeiList(val error: Boolean, @SerializedName("results") val meiList: ArrayList<Mei>)