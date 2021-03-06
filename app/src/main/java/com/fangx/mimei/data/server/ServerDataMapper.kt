package com.fangx.mimei.data.server

import com.fangx.mimei.domain.model.GankIo
import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiDetail
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

    fun convertDetailToDomain(rsp: MeiDetailList, ml_id: String): MiMeiDetail = with(rsp) {
        MiMeiDetail(error, errorMsg, convertDetailListToDomain(rsp.MeiDetails, ml_id))
    }

    private fun convertDetailListToDomain(meiDetails: HashMap<String, ArrayList<MeiDetail>>, ml_id: String): HashMap<String, List<GankIo>> {
        val map = HashMap<String, List<GankIo>>()
        for ((k, v) in meiDetails) {
            map[k] = convertMeiDetailToDomain(v, ml_id)
        }
        return map
    }

    private fun convertMeiDetailToDomain(v: ArrayList<MeiDetail>, ml_id: String): List<GankIo> = v.map {
        GankIo(ml_id, it._id, it.createdAt, it.desc, it.images, it.publishedAt, it.type, it.url, it.used, it.who)
    }
}