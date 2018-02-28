package com.fangx.mimei.data.server

import com.fangx.mimei.data.db.MiMeiDb
import com.fangx.mimei.domain.datasource.MiMeiDataSource
import com.fangx.mimei.domain.model.MiMeiDetail
import com.fangx.mimei.domain.model.MiMeiList
import org.jetbrains.anko.info

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
class GankIoServer(private val dataMapper: ServerDataMapper = ServerDataMapper(),
                   private val miMeiDb: MiMeiDb = MiMeiDb()
) : MiMeiDataSource {


    override fun requestDetail(date: String, ml_id: String): MiMeiDetail? {
        val rsp = MiMeiDetailRequest(date).execute()
        val gankIos = dataMapper.convertDetailToDomain(rsp, ml_id)
        return if (!rsp.error) {
            miMeiDb.saveGankIo(gankIos)
            miMeiDb.requestDetail(date, ml_id)
        } else {
            gankIos
        }
    }


    override fun requestList(page: Int, pageSize: Int): MiMeiList {
        return when (needInsertCount(page, pageSize)) {
            0 -> { //不需要插入 直接读取数据库数据
                miMeiDb.requestList(page, pageSize)
            }
            in 1..9 -> { //部分插入
                val rsp = MiMeiRequest(page, pageSize).execute()
                val miMeiList = dataMapper.convertListToDomain(rsp)
                miMeiDb.saveList(miMeiList, false)
                miMeiDb.requestList(page, pageSize)
            }
            else -> { //全部插入
                val rsp = MiMeiRequest(page, pageSize).execute()
                val miMeiList = dataMapper.convertListToDomain(rsp)
                miMeiDb.saveList(miMeiList)
                miMeiDb.requestList(page, pageSize)
            }
        }


        /*val rsp = MiMeiRequest(page, pageSize).execute()
        val miMeiList = dataMapper.convertListToDomain(rsp)
        return if (miMeiList.error.not() && miMeiList.errorMsg.isNotEmpty()) {
            miMeiDb.saveList(miMeiList)
            miMeiDb.requestList(page, pageSize)
        } else {
            miMeiList
        }*/

    }

    // 返回 需要插入数据库的个数
    fun needInsertCount(page: Int, pageSize: Int): Int {
        val count: Int
        val dbHistoryList = miMeiDb.requestHistoryList(page, pageSize)
        val list = MiMeiHistoryRequest().execute()
        count = if (list.isEmpty()) {
            pageSize
        } else {
            val start = (page - 1) * pageSize
            val end = Math.min(start + pageSize, list.size) //如果到最后的时候可能再加pageSize 会越界
            val historyList = list.subList(start, end)
            historyList.count { dbHistoryList.contains(it).not() }
        }
        info {
            "need insert count = $count"
        }
        return count
    }

}