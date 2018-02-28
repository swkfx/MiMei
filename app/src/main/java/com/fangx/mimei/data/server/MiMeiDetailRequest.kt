package com.fangx.mimei.data.server

import com.google.gson.Gson
import java.net.URL

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/27
 *      desc   :
 * </pre>
 */
class MiMeiDetailRequest(private val date: String) : Request<MeiDetailList>() {
    companion object {
        const val REQUEST_URL = "http://gank.io/api/day/"
    }

    override fun execute(): MeiDetailList {
        return if (isNetWorkAvailable()) {
            val rsp = URL(REQUEST_URL + date).readText()
            Gson().fromJson(rsp, MeiDetailList::class.java)
        } else {
            MeiDetailList(true,"获取数据失败,请检查网络")
        }
    }
}