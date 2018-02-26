package com.fangx.mimei.domain.commands

import com.fangx.mimei.domain.datasource.MiMeiProvider
import com.fangx.mimei.domain.model.MiMeiList

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/25
 *      desc   :
 * </pre>
 */
class RequestListCommand(private val page: Int, private val pageSize: Int, private val provider: MiMeiProvider = MiMeiProvider()) : Command<MiMeiList> {


    override fun execute(): MiMeiList = provider.requestList(page, pageSize)
}