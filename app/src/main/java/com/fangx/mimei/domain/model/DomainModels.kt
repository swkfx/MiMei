package com.fangx.mimei.domain.model

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
data class MiMeiList(val error: Boolean, val list: ArrayList<MiMei>)

data class MiMei(
        var ml_id: String,
        var title: String,
        var content: String,
        var created_at: String,
        var publishedAt: String,
        var rand_id: String,
        var updated_at: String,
        var collect: Boolean)