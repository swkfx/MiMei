package com.fangx.mimei.ui.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.fangx.mimei.R
import com.fangx.mimei.ui.adapters.HomeListAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initHomeList()

    }

    private fun initHomeList() {
        homeList.layoutManager = LinearLayoutManager(this)
        homeList.setHasFixedSize(true)
        val list = arrayListOf("111", "222", "333")
        list.add("444")
        list.add("666")
        list.add("777")
        list.add("888")
        homeList.adapter = HomeListAdapter(list)
    }
}
