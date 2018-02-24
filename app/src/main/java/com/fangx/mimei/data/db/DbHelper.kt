package com.fangx.mimei.data.db

import android.database.sqlite.SQLiteDatabase
import com.fangx.mimei.ui.base.App
import org.jetbrains.anko.db.*

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
class DbHelper : ManagedSQLiteOpenHelper(App.instance, DB_NAME, null, DB_VERSION) {

    companion object {
        const val DB_NAME = "mimei.db"
        const val DB_VERSION = 1
        val instance: DbHelper by lazy { DbHelper() }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //table mimei list
        db?.createTable(MiMeiListTable.NAME, true,
                MiMeiListTable.ID to INTEGER + PRIMARY_KEY,
                MiMeiListTable.ML_ID to TEXT,
                MiMeiListTable.CONTENT to TEXT,
                MiMeiListTable.CREATED_AT to TEXT,
                MiMeiListTable.RAND_ID to TEXT,
                MiMeiListTable.PUBLISHEDAT to TEXT,
                MiMeiListTable.UPDATED_AT to TEXT,
                MiMeiListTable.TITLE to TEXT,
                MiMeiListTable.COLLECT to INTEGER)

        db?.createTable(MiMeiDetailTable.NAME, true,
                MiMeiDetailTable.ID to INTEGER + PRIMARY_KEY,
                MiMeiDetailTable.ML_ID to TEXT,
                MiMeiDetailTable.MD_ID to TEXT,
                MiMeiDetailTable.CREATEDAT to TEXT,
                MiMeiDetailTable.DESC to TEXT,
                MiMeiDetailTable.PUBLISHEDAT to TEXT,
                MiMeiDetailTable.TYPE to TEXT,
                MiMeiDetailTable.URL to TEXT,
                MiMeiDetailTable.USED to TEXT,
                MiMeiDetailTable.WHO to TEXT)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(MiMeiListTable.NAME, true)
        db?.dropTable(MiMeiDetailTable.NAME, true)
        onCreate(db)
    }
}