package com.fangx.mimei.ui.base

import android.app.Application
import com.fangx.mimei.extensions.DelegatesExt

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
class App : Application() {

    companion object {
        var instance: App by DelegatesExt.notNullSingleValue()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

    }

    fun dp2px(dp: Int): Int {
        return (resources.displayMetrics.density * dp + .5).toInt()
    }


}