package com.fangx.mimei.domain.datasource

import com.fangx.mimei.domain.model.MiMeiDetail
import com.fangx.mimei.domain.model.MiMeiList
import org.jetbrains.anko.AnkoLogger

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
interface MiMeiDataSource : AnkoLogger {

    fun requestList(page: Int, pageSize: Int): MiMeiList?


    fun requestDetail(date: String, ml_id: String): MiMeiDetail?

}