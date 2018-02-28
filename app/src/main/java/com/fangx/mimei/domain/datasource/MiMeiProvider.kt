package com.fangx.mimei.domain.datasource

import com.fangx.mimei.data.db.MiMeiDb
import com.fangx.mimei.data.server.GankIoServer
import com.fangx.mimei.domain.model.MiMeiDetail
import com.fangx.mimei.domain.model.MiMeiList

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
class MiMeiProvider(private val sources: List<MiMeiDataSource> = SOURCE) {

    companion object {
        val SOURCE by lazy { listOf(MiMeiDb(), GankIoServer()) }
    }

    fun requestList(page: Int, pageSize: Int): MiMeiList {
        val gankIoServer = sources[1] as GankIoServer
        return if (gankIoServer.needInsertCount(page, pageSize) == 0) { //不用走api请求,db sources 在前,
            sources[0].requestList(page, pageSize) ?: MiMeiList()
        } else {
            gankIoServer.requestList(page, pageSize)
        }

    }

    fun requestDetail(date: String, ml_id: String): MiMeiDetail {
        val detailForDb = sources[0].requestDetail(date, ml_id)
        return if (detailForDb != null && !detailForDb.error) {
            detailForDb
        } else {
            sources[1].requestDetail(date, ml_id)!!
        }


    }

//    fun <T : Any> requestToSource(f: (MiMeiDataSource) -> T?): T = sources.firstResult { f(it) }


}

//private fun <T, R : Any> Iterable<T>.firstResult(function: (T) -> R?): R {
//    for (element in this) {
//        val result = function(element)
//        if (result != null) {
//            return result
//        }
//    }
//    throw NoSuchElementException("No element matching predicate was found.")
//}

