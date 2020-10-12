package com.biotecan.questionnaire.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import com.biotecan.questionnaire.R
import java.util.*


/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.view
 * @创始人: hsy
 * @创建时间: 2020/4/23 10:09
 * @类描述: 这是一个自定义方框验证码输入view
 * @修改人: hsy
 * @修改时间: 2020/4/23 10:09
 * @修改描述:
 */
class MyCodeView : AppCompatEditText {

    private val mNormalPaint: Paint = Paint()
    private val mSelectPaint: Paint = Paint()
    private val mCursorPaint: Paint = Paint()
    private var mCurrentPosition: Int = 0
    private var mEachRectLength: Float = 0f

    var mFigures = 0// 验证码个数
    var mCodeMargin = 0f// 验证码之间的间距
    var mSelectColor = 0// 选中框的颜色
    var mNormalColor = 0// 普通框的颜色
    var mBorderRadius = 0f// 边框直角的曲度
    var mBorderWidth = 0f// 边框的厚度
    var mCursorWidth = 0f// 光标宽度
    var mCursorColor = 0// 光标的颜色
    var mCursorDuration = 0// 光标闪烁的时间

    var widthResult = 0
    var heightResult = 0

    constructor (context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initAttr(context, attrs)
        initPaint()
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
    }

    @SuppressLint("Recycle")
    private fun initAttr(context: Context?, attrs: AttributeSet?) {
        val typedArray: TypedArray = context!!.obtainStyledAttributes(attrs, R.styleable.MyCodeView)
        mFigures = typedArray.getInteger(R.styleable.MyCodeView_figures, 4)
        mCodeMargin = typedArray.getDimension(R.styleable.MyCodeView_codeMargin, 20f)
        mSelectColor = typedArray.getColor(R.styleable.MyCodeView_selectBorderColor, 4)
        mNormalColor = typedArray.getColor(R.styleable.MyCodeView_normalBorderColor, 4)
        mBorderRadius = typedArray.getDimension(R.styleable.MyCodeView_borderRadius, 1f)
        mBorderWidth = typedArray.getDimension(R.styleable.MyCodeView_borderWidth, 1f)
        mCursorDuration = typedArray.getInteger(R.styleable.MyCodeView_cursorDuration, 1)
        mCursorWidth = typedArray.getDimension(R.styleable.MyCodeView_cursorWidth, 1f)
        mCursorColor = typedArray.getColor(R.styleable.MyCodeView_cursorColor, 1)
    }

    private fun initPaint() {
        mNormalPaint.isAntiAlias = true
        mNormalPaint.color = mNormalColor
        mNormalPaint.style = Paint.Style.STROKE// 空心
        mNormalPaint.strokeWidth = mBorderWidth

        mSelectPaint.isAntiAlias = true
        mSelectPaint.color = mSelectColor
        mSelectPaint.style = Paint.Style.STROKE// 空心
        mSelectPaint.strokeWidth = mBorderWidth

        mCursorPaint.isAntiAlias = true
        mCursorPaint.color = mCursorColor
        mCursorPaint.style = Paint.Style.FILL_AND_STROKE
        mCursorPaint.strokeWidth = mCursorWidth

        isFocusableInTouchMode = true// 让view的touch事件生效
        initCursorTimer()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec) //宽度测量模式
        val widthSize = MeasureSpec.getSize(widthMeasureSpec) //宽度确切数值
        widthResult = if (widthMode == MeasureSpec.EXACTLY) {
            widthSize
        } else {
            40
        }

        mEachRectLength = (widthResult - (mFigures - 1) * mCodeMargin) / mFigures

        val heightMode = MeasureSpec.getMode(heightMeasureSpec) //高度测量模式
        val heightSize = MeasureSpec.getSize(heightMeasureSpec) //高度确切数值
        heightResult = if (heightMode == MeasureSpec.EXACTLY) {
            heightSize
        } else {
            mEachRectLength.toInt()
        }

        setMeasuredDimension(widthResult, heightResult)
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        mCurrentPosition = text!!.length
        val width = mEachRectLength - paddingLeft - paddingRight
        val height = measuredHeight - paddingTop - paddingBottom
        for (i in 0 until mFigures) {
            canvas.save()
            val start = width * i + mCodeMargin * i + mBorderWidth
            var end = start + width - mBorderWidth
            if (i == mFigures - 1) {
                end -= mBorderWidth
            }
            val rect = RectF(start, mBorderWidth * 4, end, height.toFloat() - mBorderWidth)
            if (i == mCurrentPosition) {
                canvas.drawRoundRect(rect, mBorderRadius, mBorderRadius, mSelectPaint)
            } else {
                canvas.drawRoundRect(rect, mBorderRadius, mBorderRadius, mNormalPaint)
            }
            canvas.restore()
        }

        //绘制文字
        val value = text.toString()
        for (i in value.indices) {
            canvas.save()
            val start = width * i + mCodeMargin * i
            val x = start + width / 2f
            paint.textAlign = Paint.Align.CENTER
            paint.color = currentTextColor
            val fontMetrics = paint.fontMetrics
            //文字的Y位置应该从view高度减去文字高度再加上文字的一半高度位置开始
            val baseline =
                (height - fontMetrics.bottom + fontMetrics.top) - (fontMetrics.bottom + fontMetrics.top) / 2
            canvas.drawText(value[i].toString(), x, baseline, paint)
            canvas.restore()
        }

        //绘制光标
        if (!isCursorShowing && isCursorVisible && mCurrentPosition < mFigures && hasFocus()) {
            canvas.save()
            val startX = (width + mCodeMargin) * mCurrentPosition + width / 2f
            val startY = height / 3.5f
            val endX = startX
            val endY = height - height / 5f
            canvas.drawLine(startX, startY, endX, endY, mCursorPaint)
            canvas.restore()
        }
    }

    // 控制光标闪烁
    var isCursorShowing = false
    var mCursorTimerTask: TimerTask? = null
    var mCursorTimer: Timer? = null

    private fun initCursorTimer() {
        mCursorTimerTask = object : TimerTask() {
            override fun run() {
                isCursorShowing = !isCursorShowing
                postInvalidate()
            }
        }
        mCursorTimer = Timer()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event!!.apply {
            if (action == MotionEvent.ACTION_DOWN) {
                requestFocus()
                setSelection(text!!.length)
                showSoftKeyboard(context)
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mCursorTimer?.scheduleAtFixedRate(mCursorTimerTask, 0, mCursorDuration.toLong())
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mCursorTimer?.cancel()
        mCursorTimer = null
    }


    private fun showSoftKeyboard(mContext: Context?) {
        if (this.requestFocus()) {
            val imm =
                mContext!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT);
        }
    }

}