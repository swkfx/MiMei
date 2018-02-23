package com.fangx.mimei.data.server

import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.net.URL

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/11
 *      desc   :
 * </pre>
 */
class MiMeiRequest(private val page: Int, private val pageSize: Int) : AnkoLogger {
    companion object {
        val REQUEST_URL = "http://gank.io/api/history/content/"
    }

    fun excute(): MeiList {
        val jsonStr = URL(REQUEST_URL + pageSize.toString() + "/" + page.toString()).readText()
        info { jsonStr }
        return Gson().fromJson(jsonStr, MeiList::class.java)
    }
}