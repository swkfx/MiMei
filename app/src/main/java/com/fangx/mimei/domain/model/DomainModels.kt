package com.fangx.mimei.domain.model

import java.util.*

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
data class MiMeiList(val error: Boolean = true,
                     val errorMsg: String = "",
                     val list: ArrayList<MiMei> = arrayListOf()) {
    fun size(): Int = list.size

    constructor(err: Boolean, list: ArrayList<MiMei> = arrayListOf()) : this(err, "", list)
}

data class MiMei(
        var ml_id: String,
        var title: String,
        var content: String,
        var created_at: String,
        var publishedAt: String,
        var rand_id: String,
        var updated_at: String,
        var imageUrl: String,
        var collect: Boolean)


data class MiMeiDetail(val error: Boolean = true,
                       val errorMsg: String = "",
                       val dataMap: HashMap<String, List<GankIo>> = hashMapOf())

data class GankIo(
        val ml_id: String,
        val md_id: String,
        val createdAt: String,
        val desc: String,
        val images: ArrayList<String>?,
        val publishedAt: String,
        val type: String,
        val url: String,
        val used: Boolean,
        val who: String?
)