package com.fangx.mimei.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.fangx.mimei.R
import com.fangx.mimei.data.server.Mei
import com.fangx.mimei.extensions.ctx
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jsoup.Jsoup
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/1/31
 *      desc   :
 * </pre>
 */
class HomeListAdapter(var data: ArrayList<Mei>?) : RecyclerView.Adapter<HomeListAdapter.BaseHolder>(), AnkoLogger {
    companion object {
        val ITEM_VIEW = 0
        val EMPTY_VIEW = 1
        val FOOTER_VIEW = 2

        var targetWidth: Int = 0
        var targetHeight: Int = 0
    }

    var loadMoreEnable: Boolean = false //是否需要加载更多.
    private var loading: Boolean = false
    private lateinit var listener: () -> Unit

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
        else if (position == data!!.size) {
            autoLoadMore(position)
            FOOTER_VIEW
        }
        else
            super.getItemViewType(position)
    }

    override fun getItemCount(): Int {
        return if (data == null || data!!.isEmpty()) {
            1 //empty view
        } else {
            if (loadMoreEnable) {
                data!!.size + 1 // loading view
            } else {
                data!!.size
            }
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val viewType = getItemViewType(position)
        when (viewType) {
            ITEM_VIEW -> {
                holder.bindData(data!![position])
            }
            else -> {
            }
        }
    }

    inner class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(item: Mei) {
            if (itemViewType == ITEM_VIEW) {
                val time: String = formatTime(item.publishedAt)
                val title = StringBuilder()
                title.append(time).append("\t\t").append(item.title)
                itemView.title.text = title.toString()
                val imageUrl: String = Jsoup.parse(item.content).selectFirst("img").attr("src")
                val image = itemView.image
                if (targetWidth * targetHeight == 0) {
                    image.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            targetWidth = image.measuredWidth
                            targetHeight = image.measuredHeight
                            info { "targetWidth = $targetWidth , targetHeight = $targetHeight" }
                            image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            Picasso.with(itemView.ctx)
                                    .load(imageUrl)
                                    .resize(targetWidth, targetHeight)
                                    .centerCrop()
                                    .tag(itemView.ctx)
                                    .into(image)
                        }
                    })
                } else {
                    Picasso.with(itemView.ctx)
                            .load(imageUrl)
                            .resize(targetWidth, targetHeight)
                            .centerCrop()
                            .tag(itemView.ctx)
                            .into(image)

                }


            }
        }
    }

    private fun formatTime(publishedAt: String): String {
        //        2017-04-26T11:29:00.0Z
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.S'Z'", Locale.getDefault())
        val date = sdf.parse(publishedAt)
        val newSdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return newSdf.format(date)
    }

    fun add(item: Mei) {
        if (data == null) {
            data = ArrayList()
        }

        data?.add(item)
        val size = data?.size
        if (size != null) {
            notifyItemInserted(size)
        }

    }

    fun addNew(newData: ArrayList<Mei>) {
        data?.clear()
        data?.addAll(newData)
        notifyDataSetChanged()
    }

    fun add(newData: ArrayList<Mei>) {
        val size = data?.size ?: 0
        data?.addAll(newData)
        info("size=$size,newDataSize = ${newData.size}")
        notifyItemRangeInserted(size, newData.size)

    }

    fun loadMore(l: () -> Unit) {
        listener = l
    }

    fun dataSize(): Int = data?.size ?: 0

    private fun autoLoadMore(position: Int) {
        info("loadMoreEnable = $loadMoreEnable")
        info("position = $position")
        info("itemCount = $itemCount")
        info("loading = $loading")
        if (loadMoreEnable && position + 1 == itemCount && !loading) {
            info("autoLoadMore ------")
            loading = true
            listener.invoke()
        }

    }

    fun loadMoreComplete() {
        loading = false
        if (loadMoreEnable) {
            notifyItemRemoved(itemCount)
        }
    }


}