package com.fangx.mimei.ui.coustom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.text.style.ImageSpan

/**
 * <pre>
 *      author : test
 *      e-mail : fangx@hyxt.com
 *      time   : 2018/2/27
 *      desc   :
 * </pre>
 */
class CenterAlignImageSpan(context: Context?, resourceId: Int, verticalAlignment: Int) : ImageSpan(context, resourceId, verticalAlignment) {


    override fun draw(canvas: Canvas?, text: CharSequence?, start: Int, end: Int, x: Float, top: Int, y: Int, bottom: Int, paint: Paint?) {
        val fm: Paint.FontMetricsInt = paint!!.fontMetricsInt
        val transY = (y + fm.descent + y + fm.ascent) / 2f - drawable.bounds.bottom / 2f //计算y方向的位移
        canvas!!.save()
        canvas.translate(x, transY) //绘制图片位移一段距离
        drawable.draw(canvas)
        canvas.restore()

    }
}