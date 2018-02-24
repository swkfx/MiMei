package com.fangx.mimei.ui.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING
import com.fangx.mimei.R
import com.fangx.mimei.data.server.MeiList
import com.fangx.mimei.data.server.MiMeiRequest
import com.fangx.mimei.ui.adapters.HomeListAdapter
import com.fangx.mimei.ui.base.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread

class MainActivity : BaseActivity() {

    private val PAGE_SIZE: Int = 10 //一页10个

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHomeList()
        requestForNet()

    }

    private fun initPicasso(homeList: RecyclerView) {
        val picasso = Picasso.with(this)
        picasso.setIndicatorsEnabled(true)
        picasso.isLoggingEnabled = true
        homeList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                info { "newState = $newState" }
                if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                    //暂停
                    picasso.pauseTag(this@MainActivity)
                } else {
                    //继续加载
                    picasso.resumeTag(this@MainActivity)
                }
            }

        })
    }

    private fun requestForNet() {
        doAsync {
            val data = request()
            uiThread {
                if (data != null) {
                    val homeListAdapter = homeList.adapter as HomeListAdapter
                    if (data.error.not() && data.meiList.isNotEmpty()) {
                        homeListAdapter.addNew(data.meiList)
                    } else {
                        homeListAdapter.addNew(arrayListOf())
                    }
                    homeListAdapter.loadMoreEnable = homeListAdapter.dataSize() >= PAGE_SIZE
                }
            }
        }
    }

    private fun request(): MeiList? {
        return MiMeiRequest(1, PAGE_SIZE).excute()
    }

    private fun initHomeList() {
        homeList.layoutManager = LinearLayoutManager(this)
        homeList.setHasFixedSize(true)
        val homeListAdapter = HomeListAdapter(arrayListOf())
        homeList.adapter = homeListAdapter
        homeListAdapter.loadMore {
            homeList.postDelayed({
                info("loadMore...")
                doAsync {
                    val meiList = MiMeiRequest(homeListAdapter.dataSize() / PAGE_SIZE + 1, PAGE_SIZE).excute()
                    uiThread {
                        val adapter = homeList.adapter as HomeListAdapter
                        adapter.loadMoreComplete()
                        //可以根据结果改变加载是完成还是出错.
                        if (meiList.error.not()) {
                            val dataSize = adapter.dataSize()
                            adapter.add(meiList.meiList)
                            homeListAdapter.loadMoreEnable = meiList.meiList.size >= PAGE_SIZE
                            info { "dataSize = ${dataSize + 1}" }
                            homeList.scrollToPosition(dataSize)
                        }

                    }
                }
            }, 1000)
        }

        initPicasso(homeList)


    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.with(this).cancelTag(this)
    }


}