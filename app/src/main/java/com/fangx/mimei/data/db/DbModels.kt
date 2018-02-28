package com.fangx.mimei.data.db

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/24
 *      desc   :
 * </pre>
 */
class ListDbModel(val map: MutableMap<String, Any?>) {
    var ml_id: String by map
    var title: String by map
    var content: String by map
    var created_at: String by map
    var publishedAt: String by map
    var rand_id: String by map
    var updated_at: String by map
    var image_url: String by map
    var collect: Int by map

    constructor(ml_id: String,
                title: String,
                content: String,
                created_at: String,
                publishedAt: String,
                rand_id: String,
                updated_at: String,
                imageUrl: String,
                collect: Int) : this(HashMap()) {

        this.ml_id = ml_id
        this.title = title
        this.content = content
        this.created_at = created_at
        this.publishedAt = publishedAt
        this.rand_id = rand_id
        this.updated_at = updated_at
        this.image_url = imageUrl
        this.collect = collect

    }
}


class DetailDbModel(val map: MutableMap<String, Any?>) {
    var ml_id: String by map
    var md_id: String by map
    var createdAt: String by map
    var desc: String by map
    var images: String by map
    var publishedAt: String by map
    var type: String by map
    var url: String by map
    var used: Int by map
    var who: String by map


    constructor(
            ml_id: String,
            md_id: String,
            createdAt: String,
            desc: String,
            images: String,
            publishedAt: String,
            type: String,
            url: String,
            used: Int,
            who: String) : this(HashMap()) {
        this.ml_id = ml_id
        this.md_id = md_id
        this.createdAt = createdAt
        this.desc = desc
        this.images = images
        this.publishedAt = publishedAt
        this.type = type
        this.url = url
        this.used = used
        this.who = who
    }

}

