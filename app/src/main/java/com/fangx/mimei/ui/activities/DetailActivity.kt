package com.fangx.mimei.ui.activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.util.Pair
import android.support.v4.view.ViewCompat
import android.support.v7.widget.CardView
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.DynamicDrawableSpan.ALIGN_BASELINE
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fangx.mimei.R
import com.fangx.mimei.data.db.MiMeiDb
import com.fangx.mimei.domain.datasource.MiMeiProvider
import com.fangx.mimei.domain.model.GankIo
import com.fangx.mimei.domain.model.MiMei
import com.fangx.mimei.domain.model.MiMeiDetail
import com.fangx.mimei.extensions.Utils
import com.fangx.mimei.ui.base.BaseActivity
import com.fangx.mimei.ui.coustom.CenterAlignImageSpan
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_detail.*
import org.jetbrains.anko.*

class DetailActivity : BaseActivity() {

    companion object {
        const val KEY_ML_ID = "key_ml_id"
        const val KEY_SHARE_ID = "key_share_id"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val mlId = intent.getStringExtra(KEY_ML_ID)
        initData(mlId)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initData(mlId: String) {
        val miMeiDb = MiMeiDb()
        val miMei = miMeiDb.findMiMeiBy(mlId)
        if (miMei == null) {
            toast("数据异常")
            onBackPressed()
        }
        val date = Utils.formatParam(miMei!!.publishedAt)
        doAsync {
            val detail = MiMeiProvider().requestDetail(date, mlId)
            uiThread {
                if (detail.error.not()) {
                    initView(detail, miMei)
                } else {
                    toast(detail.errorMsg)
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView(detail: MiMeiDetail, miMei: MiMei) {
        val meiDetails = detail.dataMap
        for ((key, value) in meiDetails) {
            when (key) {
                "福利" -> {
                    val title: String = miMei.title
                    val cardView = getMiMeiView(value[0].url, title)
                    cardView.setOnClickListener {
//                        startImageActivity(value[0].url)

                        val intent = Intent(this@DetailActivity, ImageActivity::class.java)
                        intent.putExtra(ImageActivity.KEY_IMG_URL, value[0].url)
                        val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this@DetailActivity,
                                Pair(cardView.findViewById(R.id.mimei_image), KEY_SHARE_ID))
                        ActivityCompat.startActivity(this@DetailActivity, intent, optionsCompat.toBundle())

                    }
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


        val image = contentLayout.findViewById<ImageView>(R.id.mimei_image)
        if (image != null) {
            info { "image != null" }
            ViewCompat.setTransitionName(image, KEY_SHARE_ID)
        } else {
            info { "image == null" }
        }

    }

    private fun startImageActivity(url: String) {
        startActivity<ImageActivity>(ImageActivity.KEY_IMG_URL to url)
    }


    private fun getMiMeiView(imageUrl: String, title: String): CardView {
        val imageView = ImageView(this)
        imageView.id = R.id.mimei_image
        //根据屏幕尺寸,重新动态计算图片的宽高
        val size = doAsyncResult {
            var bitmap: Bitmap?
            bitmap = try {
                Picasso.get()
                        .load(imageUrl)
                        .get()
            } catch (e: Exception) {
                e.printStackTrace()
                Picasso.get()
                        .load(R.drawable.img_place_holder)
                        .get()
            }
            listOf(bitmap.width, bitmap.height)
        }.get()
        info {
            "width=${size[0]} , height=${size[1]}"
        }
        val padding = (displayMetrics.density * 8).toInt()
        val margin = (displayMetrics.density * 12).toInt()
        val imageTargetWidth = displayMetrics.widthPixels - (margin + padding) * 2
        val scale: Float = imageTargetWidth / size[0].toFloat()
        val imageTargetHeight = (size[1] * scale).toInt()
        info {
            "scale=$scale , imageTargetWidth=$imageTargetWidth , imageTargetHeight=$imageTargetHeight"
        }
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.img_place_holder_2)
                .resize(imageTargetWidth, imageTargetHeight)
                .centerCrop()
                .into(imageView)
        val layoutParams = LinearLayout.LayoutParams(matchParent, wrapContent)
        //                    layoutParams.gravity = Gravity.CENTER
        layoutParams.margin = margin
        val cardView = CardView(this)
        cardView.id = R.id.card_view
        cardView.layoutParams = layoutParams
        cardView.setContentPadding(padding, padding, padding, padding)
        val container = LinearLayout(this)
        container.orientation = LinearLayout.VERTICAL
        container.addView(getTitleView(title))
        container.addView(imageView)
        cardView.addView(container)

        ViewCompat.setTransitionName(imageView, KEY_SHARE_ID)
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

    private fun getItemTextView(item: GankIo): View {
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
