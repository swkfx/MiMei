package com.fangx.mimei.data.server

import android.util.Log
import com.fangx.mimei.extensions.DelegatesExt
import com.fangx.mimei.extensions.fromJson
import com.fangx.mimei.ui.base.App
import com.google.gson.Gson
import java.net.URL

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   : 检查是否需要去获取Gank.io 的列表
 * </pre>
 */
class MiMeiHistoryRequest : Request<List<String>>() {

    companion object {
        const val REQUEST_URL = "http://gank.io/api/day/history"
        const val KEY_HISTORY_LIST = "key_history_list"
        const val DEFAULT_HISTORY_LIST = ""
        const val KEY_HISTORY_REQUEST_TIME = "key_history_request_time"
        const val DEFAULT_HISTORY_REQUEST_TIME = 0L
        const val DIFF_TIME = 1000 * 60 * 60 * 6L  //6H
    }

    var localHistoryList: String by DelegatesExt.preference(App.instance, KEY_HISTORY_LIST, DEFAULT_HISTORY_LIST)

    var historyReqTime: Long by DelegatesExt.preference(App.instance, KEY_HISTORY_REQUEST_TIME, DEFAULT_HISTORY_REQUEST_TIME)

    /**
     * return true 需要从api获取. false 不需要
     * */
    override fun execute(): List<String> {

        //判断当前日期是否请求过历史列表.
        val currentTimeMillis = System.currentTimeMillis()
        return if (isNetWorkAvailable() && currentTimeMillis - historyReqTime > DIFF_TIME) { //超过6小时就再更新下历史列表
            val gson = Gson()
            val rsp = URL(REQUEST_URL).readText()
            Log.d("TAG", rsp)
            val (error, historyListForNet) = gson.fromJson<HistoryDateList>(rsp, HistoryDateList::class.java)
            if (!error) {
                localHistoryList = gson.toJson(historyListForNet)
                historyReqTime = currentTimeMillis
                Log.d("TAG", "historyReqTime = $historyReqTime")
                historyListForNet
            } else {
                gson.fromJson(localHistoryList)
            }
        } else {
            if (localHistoryList.isEmpty()) {
                emptyList()
            } else {
                Gson().fromJson(localHistoryList)
            }
        }
    }


}