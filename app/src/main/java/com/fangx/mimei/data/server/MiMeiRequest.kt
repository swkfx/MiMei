package com.fangx.mimei.data.server

import android.content.Context
import android.net.ConnectivityManager
import com.fangx.mimei.ui.base.App
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
class MiMeiRequest(private val page: Int, private val pageSize: Int) : Request<MeiList>(), AnkoLogger {
    companion object {
        const val REQUEST_URL = "http://gank.io/api/history/content/"
    }


    override fun execute(): MeiList {
        val service = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (null == service.activeNetworkInfo || service.activeNetworkInfo.isAvailable.not()) {
            return MeiList(true, "网络不可用")
        }
        val jsonStr = URL(REQUEST_URL + pageSize.toString() + "/" + page.toString()).readText()
        info { jsonStr }
        return Gson().fromJson(jsonStr, MeiList::class.java)
    }
}