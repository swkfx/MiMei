package com.fangx.mimei.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fangx.mimei.R
import com.fangx.mimei.extensions.ctx
import kotlinx.android.synthetic.main.item_home_list.view.*

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/1/31
 *      desc   :
 * </pre>
 */
class HomeListAdapter(var data: ArrayList<String>?) : RecyclerView.Adapter<HomeListAdapter.BaseHolder>() {
    companion object {
        val ITEM_VIEW = 0
        val EMPTY_VIEW = 1
        val FOOTER_VIEW = 2
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when (viewType) {
            ITEM_VIEW -> {
                val itemView = LayoutInflater.from(parent.ctx).inflate(R.layout.item_home_list, parent, false)
                BaseHolder(itemView)
            }
            EMPTY_VIEW -> {
                val emptyView = LayoutInflater.from(parent.ctx).inflate(R.layout.item_empty_view, parent, false)
                BaseHolder(emptyView)
            }
            else -> {
                val footerView = LayoutInflater.from(parent.ctx).inflate(R.layout.item_footer_view, parent, false)
                BaseHolder(footerView)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (data == null || data!!.isEmpty())
            EMPTY_VIEW
        else if (position == data!!.size)
            FOOTER_VIEW
        else
            super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if (data == null || data!!.isEmpty()) {
            1 //empty view
        } else {
            data!!.size
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            ITEM_VIEW -> {
                holder.bindData(data?.get(position))
            }
            else -> {
            }
        }
    }

    inner class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(item: String?) {
            if (itemViewType == ITEM_VIEW) {
                itemView.title.text = item
            }
        }
    }

    fun addItem(item: String) {
        if (data == null) {
            data = ArrayList<String>()
        }
        if (item.isEmpty()) {
            data?.add(item)
        }
        val size = data?.size
        if (size != null) {
            notifyItemInserted(size)
        }


    }

    fun loadMore(text: String = "loading...") {

    }


}