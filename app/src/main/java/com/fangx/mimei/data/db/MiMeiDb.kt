package com.fangx.mimei.data.db

import android.database.sqlite.SQLiteDatabase
import com.fangx.mimei.domain.datasource.MiMeiDataSource
import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiDetail
import com.fangx.mimei.domain.model.MiMeiList
import com.google.gson.Gson
import org.jetbrains.anko.db.*

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
class MiMeiDb(
        private val dbHelper: DbHelper = DbHelper.instance,
        private val dataMapper: DbDataMapper = DbDataMapper()
) : MiMeiDataSource {


    override fun requestList(page: Int, pageSize: Int) = dbHelper.use {
        val parseList = select(MiMeiListTable.NAME)
                .orderBy(MiMeiListTable.PUBLISHEDAT, SqlOrderDirection.DESC)
                .limit((page - 1) * pageSize, pageSize)
                .parseList(object : MapRowParser<ListDbModel> {
                    override fun parseRow(columns: Map<String, Any?>): ListDbModel {
                        return ListDbModel(HashMap(columns))
                    }

                })
        parseList.let {
            dataMapper.convertToDomain(it)
        }
    }

    fun requestHistoryList(page: Int, pageSize: Int) = dbHelper.use {
        val parseList = select(MiMeiListTable.NAME, MiMeiListTable.PUBLISHEDAT)
                .orderBy(MiMeiListTable.PUBLISHEDAT, SqlOrderDirection.DESC)
                .limit((page - 1) * pageSize, pageSize)
                .parseList(object : RowParser<String> {
                    override fun parseRow(columns: Array<Any?>): String {
                        return columns[0] as String
                    }
                })
        parseList.let {
            dataMapper.convertHistoryToDomain(it)
        }

    }

    fun saveList(miMeiList: MiMeiList, isAllInsert: Boolean = true) = dbHelper.use {
        if (isAllInsert) {
            miMeiList.list.forEach {
                insert(it)
            }
        } else {
            miMeiList.list.forEach {
                insertIfNotExist(it)
            }
        }
    }

    private fun SQLiteDatabase.insertIfNotExist(it: MiMei) {
        val opt = findMiMeiBy(it.ml_id)
        if (opt == null) {
            insert(it)
        }
    }

    private fun SQLiteDatabase.insert(it: MiMei) {
        insert(MiMeiListTable.NAME,
                Pair(MiMeiListTable.ML_ID, it.ml_id),
                MiMeiListTable.TITLE to it.title,
                MiMeiListTable.CONTENT to it.content,
                MiMeiListTable.CREATED_AT to it.created_at,
                MiMeiListTable.PUBLISHEDAT to it.publishedAt,
                MiMeiListTable.RAND_ID to it.rand_id,
                MiMeiListTable.UPDATED_AT to it.updated_at,
                MiMeiListTable.IMAGE_URL to it.imageUrl,
                MiMeiListTable.COLLECT to if (it.collect) 1 else 0)
    }

    fun updateCollect(it: MiMei) = dbHelper.use {
        val sql = "${MiMeiListTable.ML_ID} = '${it.ml_id}'"
        update(MiMeiListTable.NAME,
                MiMeiListTable.COLLECT to if (it.collect) 1 else 0).whereArgs(sql).exec()
    }

    fun findMiMeiBy(mlId: String) = dbHelper.use {
        val sql = "${MiMeiListTable.ML_ID} = '$mlId'"
        val opt = select(MiMeiListTable.NAME)
                .whereSimple(sql)
                .parseOpt(object : MapRowParser<ListDbModel> {
                    override fun parseRow(columns: Map<String, Any?>): ListDbModel {
                        return ListDbModel(HashMap(columns))
                    }

                })
        opt?.let {
            dataMapper.convertDbMiMeiToDomain(it)
        }

    }

    override fun requestDetail(date: String, ml_id: String) = dbHelper.use {
        val sql = "${MiMeiDetailTable.ML_ID} = ?"
        val opts = select(MiMeiDetailTable.NAME)
                .whereSimple(sql, ml_id)
                .orderBy(MiMeiDetailTable.ID)
                .parseList(object : MapRowParser<DetailDbModel> {
                    override fun parseRow(columns: Map<String, Any?>): DetailDbModel {
                        return DetailDbModel(HashMap(columns))
                    }

                })
        opts.let {
            dataMapper.convertDbDetailToDomain(it)
        }
    }

    fun saveGankIo(gankIos: MiMeiDetail) = dbHelper.use {
        gankIos.dataMap.values.forEach {
            it.forEach {
                var imageUrls: String? = null
                if (it.images != null && !it.images.isEmpty()) {
                    imageUrls = Gson().toJson(it.images)
                }
                val i = insert(MiMeiDetailTable.NAME,
                        MiMeiDetailTable.ML_ID to it.ml_id,
                        MiMeiDetailTable.MD_ID to it.md_id,
                        MiMeiDetailTable.CREATEDAT to it.createdAt,
                        MiMeiDetailTable.DESC to it.desc,
                        MiMeiDetailTable.IMAGES to imageUrls,
                        MiMeiDetailTable.PUBLISHEDAT to it.publishedAt,
                        MiMeiDetailTable.TYPE to it.type,
                        MiMeiDetailTable.URL to it.url,
                        MiMeiDetailTable.USED to it.used,
                        MiMeiDetailTable.WHO to it.who
                )
                println("insert = $i")
            }
        }
    }
}