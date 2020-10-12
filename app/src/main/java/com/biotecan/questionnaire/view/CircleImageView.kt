package com.biotecan.questionnaire.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.view
 * @创始人: hsy
 * @创建时间: 2020/5/6 14:22
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/6 14:22
 * @修改描述:
 */
class CircleImageView : AppCompatImageView {

    private var mScale: Float = 0.0f
    private lateinit var mPaint: Paint
    private var mRadius: Float = 0f

    constructor(context: Context) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = measuredWidth.coerceAtMost(measuredHeight)
        mRadius = size / 2f
        setMeasuredDimension(size, size)
    }

    override fun onDraw(canvas: Canvas?) {
        mPaint = Paint()
        if (null != drawable && drawable.intrinsicWidth > 0) {
            val bitmap = (drawable as BitmapDrawable).toBitmap()
            val bitmapShader =
                BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            mScale = (mRadius * 2.0f) / bitmap.height.coerceAtMost(bitmap.width);
            val matrix = Matrix()
            matrix.setScale(mScale, mScale)
            bitmapShader.setLocalMatrix(matrix)
            mPaint.shader = bitmapShader
            canvas?.drawCircle(mRadius, mRadius, mRadius, mPaint)
        } else {
            super.onDraw(canvas)
        }
    }
}