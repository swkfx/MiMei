package com.fangx.mimei.data.server

import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiList
import org.jsoup.Jsoup

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
class ServerDataMapper {
    fun convertListToDomain(result: MeiList): MiMeiList = with(result) {
        MiMeiList(error, errorMsg, convertMeisToDomain(meiList))
    }

    private fun convertMeisToDomain(meiList: ArrayList<Mei>): ArrayList<MiMei> {
        val list = arrayListOf<MiMei>()
        if (meiList.isNotEmpty()) {
            meiList.forEach {
                list.add(convertMeiToDomain(it))
            }
        }
        return list
    }

    private fun convertMeiToDomain(it: Mei): MiMei = with(it) {
        val imageUrl: String = Jsoup.parse(content).selectFirst("img").attr("src")
        MiMei(_id, title, content, created_at, publishedAt, rand_id, updated_at, imageUrl, false)
    }
}