package com.fangx.mimei.data.db

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
object MiMeiListTable {
    const val NAME = "tb_mimei_list"
    const val ID = "_id" //数据库自增长id
    const val ML_ID = "ml_id" // 此条数据自己的id
    const val CONTENT = "content"
    const val CREATED_AT = "created_at"
    const val PUBLISHEDAT = "publishedAt"
    const val RAND_ID = "rand_id"
    const val TITLE = "title"
    const val UPDATED_AT = "updated_at"
    const val COLLECT = "collect"
    const val IMAGE_URL = "image_url"
}

object MiMeiDetailTable {
    const val NAME = "tb_mimei_detail"
    const val ID = "_id" //数据库自增长id
    const val ML_ID = "ml_id" // 对应的列表id
    const val MD_ID = "md_id" // 此条数据自己的id
    const val CREATEDAT = "createdAt"
    const val DESC = "desc"
    const val IMAGES = "images"
    const val PUBLISHEDAT = "publishedAt"
    const val TYPE = "type"
    const val URL = "url"
    const val USED = "used"
    const val WHO = "who"
}