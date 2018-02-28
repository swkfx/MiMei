package com.fangx.mimei.ui.activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.CardView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.DynamicDrawableSpan.ALIGN_BASELINE
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fangx.mimei.R
import com.fangx.mimei.data.db.MiMeiDb
import com.fangx.mimei.data.server.MeiDetail
import com.fangx.mimei.data.server.MeiDetailList
import com.fangx.mimei.data.server.MiMeiDetailRequest
import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.extensions.Utils
import com.fangx.mimei.ui.base.BaseActivity
import com.fangx.mimei.ui.coustom.CenterAlignImageSpan
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.*

class DetailActivity : BaseActivity() {

    companion object {
        const val KEY_ML_ID = "key_ml_id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val mlId = intent.getStringExtra(KEY_ML_ID)
        initData(mlId)


    }

    private fun initData(mlId: String) {
        val miMeiDb = MiMeiDb()
        val miMei = miMeiDb.findMiMeiBy(mlId)
        if (miMei == null) {
            toast("数据异常")
            onBackPressed()
        }
        val reqParams = Utils.formatParam(miMei!!.publishedAt)
        doAsync {
            val detail = MiMeiDetailRequest(reqParams).execute()
            uiThread {
                if (detail.error.not()) {
                    initView(detail, miMei)
                } else {
                    toast("请求失败")
                }
            }
        }
    }

    private fun initView(detail: MeiDetailList, miMei: MiMei) {
        val meiDetails = detail.MeiDetails
        for ((key, value) in meiDetails) {
            when (key) {
                "福利" -> {
                    val title: String = miMei.title
                    val cardView = getMiMeiView(value[0].url, title)
                    contentLayout.addView(cardView, 0)
                }
                else -> {
                    contentLayout.addView(getTypeTextView(key))
                    value.forEach {
                        contentLayout.addView(getItemTextView(it))
                    }
                }
            }
        }
    }


    private fun getMiMeiView(imageUrl: String, title: String): CardView {
        val imageView = ImageView(this)
        //根据屏幕尺寸,重新动态计算图片的宽高
        val size = doAsyncResult {
            val bitmap = Picasso.with(this@DetailActivity)
                    .load(imageUrl).get()
            if (bitmap != null) {
                val width = bitmap.width
                val height = bitmap.height
                if (width > height) {
                }
            }
            listOf(bitmap.width, bitmap.height)
        }.get()
        info {
            "width=${size[0]} , height=${size[1]}"
        }
        val padding = (displayMetrics.density * 4).toInt()
        val margin = (displayMetrics.density * 12).toInt()
        val imageTargetWidth = displayMetrics.widthPixels - (margin + padding) * 2
        val scale: Float = imageTargetWidth / size[0].toFloat()
        val imageTargetHeight = (size[1] * scale).toInt()
        info {
            "scale=$scale , imageTargetWidth=$imageTargetWidth , imageTargetHeight=$imageTargetHeight"
        }
        Picasso.with(this)
                .load(imageUrl)
                .placeholder(R.drawable.img_place_holder_2)
                .resize(imageTargetWidth, imageTargetHeight)
                .centerCrop()
                .into(imageView)
        val layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
        //                    layoutParams.gravity = Gravity.CENTER
        layoutParams.margin = margin
        val cardView = CardView(this)
        cardView.layoutParams = layoutParams
        cardView.setContentPadding(padding, padding, padding, padding)
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.addView(getTitleView(title))
        container.addView(imageView)
        cardView.addView(container)
        return cardView
    }

    private fun getTitleView(title: String): View {
        val tv = TextView(this)
        tv.text = title
        tv.textSize = 20f
//        tv.paint.isFakeBoldText = true
        tv.setTextColor(Color.parseColor("#3C3F41")) //6897BB
        val layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
        layoutParams.bottomMargin = (displayMetrics.density * 8).toInt()
        tv.layoutParams = layoutParams
        return tv
    }

    private fun getTypeTextView(key: String): View {
        val tv = TextView(this)
        tv.text = key
        tv.textSize = 20f
        tv.setTextColor(Color.parseColor("#3C3F41"))
        val layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
        layoutParams.leftMargin = (displayMetrics.density * 8).toInt()
        layoutParams.bottomMargin = (displayMetrics.density * 8).toInt()
        layoutParams.topMargin = (displayMetrics.density * 12).toInt()

        layoutParams.gravity = Gravity.CENTER_VERTICAL
        tv.layoutParams = layoutParams
        return tv
    }

    private fun getItemTextView(item: MeiDetail): View {
        val ssb = SpannableStringBuilder("• ")

        addColorSpan(ssb, item.desc, "#6897BB")

        if (!TextUtils.isEmpty(item.who)) {
            addColorSpan(ssb, "  (${item.who})", "#999999", 16)
        }

        if (item.images != null && item.images.isNotEmpty()) {
            addImageSpanAtEnd(ssb)
        }


        val tv = TextView(this)
        tv.text = ssb
        tv.textSize = 18f
        tv.setTextColor(Color.parseColor("#000000"))
        val layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
        layoutParams.leftMargin = (displayMetrics.density * 16).toInt()
        layoutParams.bottomMargin = (displayMetrics.density * 8).toInt()
        layoutParams.gravity = Gravity.CENTER_VERTICAL
        tv.layoutParams = layoutParams
        tv.setOnClickListener {
            val uri = Uri.parse(item.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }
        return tv
    }

    private fun addImageSpanAtEnd(ssb: SpannableStringBuilder) {
        val image = " image"
        val imageSpan = CenterAlignImageSpan(this, R.drawable.img_place_holder_small, ALIGN_BASELINE)
        val start = ssb.length
        val end = start + image.length
        ssb.append(image)
        //留个空格位置
        ssb.setSpan(imageSpan, start + 1, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }

    private fun addColorSpan(ssb: SpannableStringBuilder, text: String, color: String, size: Int = 18) {
        val colorSpan = ForegroundColorSpan(Color.parseColor(color))
        val sizeSpan = AbsoluteSizeSpan(size, true)
        val start = ssb.length
        val end = start + text.length
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ssb.append(text, colorSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            ssb.append(text)
            ssb.setSpan(colorSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        if (size != 18) {
            ssb.setSpan(sizeSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }


}
