package com.fangx.mimei.data.server

import android.content.Context
import android.net.ConnectivityManager
import com.fangx.mimei.ui.base.App

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
abstract class Request<out T> {

    abstract fun execute(): T

    /**
     * return true 网络可用,false 不可用
     * */
    fun isNetWorkAvailable(): Boolean {
        val service = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return null != service.activeNetworkInfo && service.activeNetworkInfo.isAvailable
    }
}