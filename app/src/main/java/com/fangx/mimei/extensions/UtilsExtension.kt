package com.fangx.mimei.extensions

import android.text.TextUtils

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