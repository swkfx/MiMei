package com.fangx.mimei.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.fangx.mimei.R
import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.extensions.Utils
import com.fangx.mimei.extensions.ctx
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_home_list.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.lang.StringBuilder
import java.util.*

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/1/31
 *      desc   :
 * </pre>
 */
class HomeListAdapter(var data: ArrayList<MiMei>?) : RecyclerView.Adapter<HomeListAdapter.BaseHolder>(), AnkoLogger {
    companion object {
        const val ITEM_VIEW = 0
        const val EMPTY_VIEW = 1
        const val FOOTER_VIEW = 2

        var targetWidth: Int = 0
        var targetHeight: Int = 0
    }

    var loadMoreEnable: Boolean = false //是否需要加载更多.
    private var loading: Boolean = false
    private lateinit var listener: () -> Unit
    private lateinit var itemClickListener: (position: Int, view: View, adapter: RecyclerView.Adapter<HomeListAdapter.BaseHolder>) -> Unit

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
                holder.bindData(position, data!![position])
            }
            else -> {
            }
        }
    }

    inner class BaseHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindData(position: Int, item: MiMei) {
            if (itemViewType == ITEM_VIEW) {
                val time: String = Utils.formatTime(item.publishedAt)
                val title = StringBuilder()
                title.append(time).append("\t\t").append(item.title)
                itemView.title.text = title.toString()
                //这个是个耗时操作 不能放在这里做 会引起卡顿.
                //val image_url: String = Jsoup.parse(item.content).selectFirst("img").attr("src")
                val image = itemView.image
                if (targetWidth * targetHeight == 0) {
                    image.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                        override fun onGlobalLayout() {
                            targetWidth = image.measuredWidth
                            targetHeight = image.measuredHeight
                            info { "targetWidth = $targetWidth , targetHeight = $targetHeight" }
                            image.viewTreeObserver.removeOnGlobalLayoutListener(this)
                            Picasso.get()
                                    .load(item.imageUrl)
                                    .placeholder(R.drawable.img_place_holder_2)
                                    .resize(targetWidth, targetHeight)
                                    .centerCrop()
                                    .tag(itemView.ctx)
                                    .into(image)
                        }
                    })
                } else {
                    Picasso.get()
                            .load(item.imageUrl)
                            .placeholder(R.drawable.img_place_holder_2)
                            .resize(targetWidth, targetHeight)
                            .centerCrop()
                            .tag(itemView.ctx)
                            .into(image)

                }
                itemView.ivCollect.isSelected = item.collect

                //listener
                itemView.ivCollect.setOnClickListener {
                    itemClickListener.invoke(position, it, this@HomeListAdapter)
                }
                itemView.setOnClickListener {
                    itemClickListener.invoke(position, it, this@HomeListAdapter)
                }

            }
        }
    }

    fun add(item: MiMei) {
        if (data == null) {
            data = ArrayList()
        }

        data?.add(item)
        val size = data?.size
        if (size != null) {
            notifyItemInserted(size)
        }

    }

    fun addNew(newData: ArrayList<MiMei>) {
        data?.clear()
        data?.addAll(newData)
        notifyDataSetChanged()
    }

    fun add(newData: ArrayList<MiMei>) {
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

    fun setOnItemClickListener(l: (position: Int, view: View, adapter: RecyclerView.Adapter<HomeListAdapter.BaseHolder>) -> Unit) {
        this.itemClickListener = l
    }

}