package com.fangx.mimei.extensions

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/1
 *      desc   :
 * </pre>
 */
fun String.isNotEmpty(str: String): Boolean {
    return !TextUtils.isEmpty(str)
}

fun String.isEmpty(str: String): Boolean {
    return TextUtils.isEmpty(str)
}

//fun String.toFormatTime(): String {
//    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.getDefault())
//    val date = sdf.parse(this)
//    val newSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    return newSdf.format(date)
//}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object : TypeToken<T>() {}.type)