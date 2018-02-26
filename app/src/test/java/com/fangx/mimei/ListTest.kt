package com.fangx.mimei

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/26
 *      desc   :
 * </pre>
 */
class ListTest(val list: List<String> = LIST) {

    companion object {
        val LIST = listOf<String>("A", "B")
    }
}