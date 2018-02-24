package com.fangx.mimei.data.server

import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiList

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
        MiMeiList(error, convertMeisToDomain(meiList))
    }

    private fun convertMeisToDomain(meiList: ArrayList<Mei>): ArrayList<MiMei> {
        val list = arrayListOf<MiMei>()
        meiList.forEach {
            list.add(convertMeiToDomain(it))
        }
        return list
    }

    private fun convertMeiToDomain(it: Mei): MiMei = with(it) {
        MiMei(_id, title, content, created_at, publishedAt, rand_id, updated_at, false)
    }
}