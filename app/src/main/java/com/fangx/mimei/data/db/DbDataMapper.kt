package com.fangx.mimei.data.db

import android.text.TextUtils
import com.fangx.mimei.domain.model.GankIo
import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiDetail
import com.fangx.mimei.domain.model.MiMeiList
import com.fangx.mimei.extensions.Utils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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

    fun convertDbDetailToDomain(it: List<DetailDbModel>): MiMeiDetail {
        return if (it.isNotEmpty()) {
            MiMeiDetail(false, "", converDbDetailListToDomain(it))
        } else {
            MiMeiDetail(true, "本地没有查到数据")
        }
    }

    private fun converDbDetailListToDomain(it: List<DetailDbModel>): HashMap<String, List<GankIo>> {
        val map = HashMap<String, List<GankIo>>()
        val gson = Gson()
        val gankIo = it.map {
            with(it) {
                map[it.type] = emptyList()
                var imageUrls: ArrayList<String> = arrayListOf()
                if (!TextUtils.isEmpty(images)) {
                    val typeToken = object : TypeToken<ArrayList<String>>() {}.type
                    imageUrls = gson.fromJson(images, typeToken)
                }

                GankIo(ml_id, md_id, createdAt, desc, imageUrls, publishedAt, type, url, used > 0, who)
            }
        }
        if (map.keys.isNotEmpty()) {
            map.keys.forEach {
                val k = it
                map[k] = gankIo.filter {
                    it.type == k
                }
            }
        }
        return map

    }
}