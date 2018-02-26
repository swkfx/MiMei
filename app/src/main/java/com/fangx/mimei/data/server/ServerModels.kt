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

data class MeiList(val error: Boolean = true,
                   val errorMsg: String = "未知错误",
                   @SerializedName("results") val meiList: ArrayList<Mei> = arrayListOf())


/*"_id":"5a8a904d421aa91331a69d82",
  "createdAt":"2018-02-19T16:52:29.771Z",
  "desc":"GarlandView seamlessly transitions between multiple lists of content. Made by @Ramotion",
  "images":[
      "http://img.gank.io/87e49a0b-feb8-47b2-b731-d9fc3cd6f485"
  ],
  "publishedAt":"2018-02-22T08:24:35.209Z",
  "source":"web",
  "type":"iOS",
  "url":"https://github.com/Ramotion/garland-view",
  "used":true,
  "who":"Alex Mikhnev" */
//url ->http://gank.io/api/day/2018/02/22
data class MeiDetailList(val error: Boolean, @SerializedName("results") val MeiDetails: HashMap<String, ArrayList<MeiDetail>>)

data class MeiDetail(
        val _id: String,
        val createdAt: String,
        val desc: String,
        val images: ArrayList<String>,
        val publishedAt: String,
        val source: String,
        val type: String,
        val url: String,
        val used: Boolean,
        val who: String)

data class HistoryDateList(val error: Boolean, @SerializedName("results") val historyList: ArrayList<String>)