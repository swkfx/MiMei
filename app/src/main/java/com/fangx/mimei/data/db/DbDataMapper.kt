package com.fangx.mimei.data.db

import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiList
import com.fangx.mimei.extensions.Utils

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
class DbDataMapper {
    fun convertToDomain(list: List<ListDbModel>): MiMeiList {
        return if (list.isNotEmpty()) {
            val miMeiList = list.map { convertDbMiMeiToDomain(it) }
            MiMeiList(false, ArrayList(miMeiList))
        } else {
            MiMeiList(true, "没有找到本地数据")
        }

    }

    fun convertDbMiMeiToDomain(it: ListDbModel): MiMei = with(it) {

        MiMei(ml_id, title, content, created_at, publishedAt, rand_id, updated_at, image_url, collect > 0)
    }

    fun convertHistoryToDomain(it: List<String>): List<String> = it.map {
        Utils.formatTime(it)
    }
}