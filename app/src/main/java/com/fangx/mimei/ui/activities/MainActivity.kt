package com.fangx.mimei.ui.activities

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING
import android.widget.ImageView
import com.fangx.mimei.R
import com.fangx.mimei.data.db.MiMeiDb
import com.fangx.mimei.domain.commands.RequestListCommand
import com.fangx.mimei.ui.adapters.HomeListAdapter
import com.fangx.mimei.ui.base.BaseActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.*

class MainActivity : BaseActivity() {

    private val PAGE_SIZE: Int = 10 //一页10个

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHomeList()
        requestForNet()

    }

    private fun initPicasso(homeList: RecyclerView) {
        val picasso = Picasso.get()
        //        picasso.setIndicatorsEnabled(true)//显示角标
        //        picasso.isLoggingEnabled = true //显示日志
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
            val data = RequestListCommand(1, PAGE_SIZE).execute()
            uiThread {
                val homeListAdapter = homeList.adapter as HomeListAdapter
                if (data.error.not() && data.list.isNotEmpty()) {
                    homeListAdapter.addNew(data.list)
                } else {
                    toast(data.errorMsg)
                    homeListAdapter.addNew(arrayListOf())
                }
                homeListAdapter.loadMoreEnable = homeListAdapter.dataSize() >= PAGE_SIZE
            }
        }
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
                    val data = RequestListCommand(homeListAdapter.dataSize() / PAGE_SIZE + 1, PAGE_SIZE).execute()
                    uiThread {
                        val adapter = homeList.adapter as HomeListAdapter
                        adapter.loadMoreComplete()
                        //可以根据结果改变加载是完成还是出错.
                        if (data.error.not()) {
                            val dataSize = adapter.dataSize()
                            adapter.add(data.list)
                            homeListAdapter.loadMoreEnable = data.list.size >= PAGE_SIZE
                            info { "dataSize = ${dataSize + 1}" }
                            homeList.scrollToPosition(dataSize)
                        } else {
                            toast(data.errorMsg)
                        }


                    }
                }
            }, 1000)
        }
        homeListAdapter.setOnItemClickListener { position, view, _ ->
            when (view.id) {
                R.id.cv_list_item -> { //click item
                    //启动详情页
                    val item = homeListAdapter.data?.get(position)
                    startActivity<DetailActivity>(DetailActivity.KEY_ML_ID to item?.ml_id)
                }
                R.id.ivCollect -> {
                    val item = homeListAdapter.data?.get(position)
                    item?.collect = !item!!.collect
                    val ivCollect = view as ImageView
                    ivCollect.isSelected = item.collect
                    //保存收藏
                    val db = MiMeiDb()
                    db.updateCollect(item)
                }
                else -> {
                }
            }
        }
        initPicasso(homeList)


    }

    override fun onDestroy() {
        super.onDestroy()
        Picasso.get().cancelTag(this)
    }


}
