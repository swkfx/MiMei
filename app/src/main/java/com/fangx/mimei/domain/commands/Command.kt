package com.fangx.mimei.domain.commands

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
interface Command<out T> {
    fun execute(): T
}