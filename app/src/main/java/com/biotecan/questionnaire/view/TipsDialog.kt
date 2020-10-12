package com.biotecan.questionnaire.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.biotecan.questionnaire.R
import kotlinx.android.synthetic.main.tips_layout.*

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.view
 * @创始人: hsy
 * @创建时间: 2020/5/13 9:26
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/13 9:26
 * @修改描述:
 */
class TipsDialog(context: Context) : Dialog(context, R.style.TipsDialog) {

    private var mTipsOnClickListener: TipsOnClickListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tips_layout)
        setCanceledOnTouchOutside(false)
        val str = SpannableStringBuilder(context.getString(R.string.tips_text))
        val span = ForegroundColorSpan(ContextCompat.getColor(context, R.color.tips_text))
        str.setSpan(span, 2, 4, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tips_text.text = str
        tips_btn.setOnClickListener {
            mTipsOnClickListener?.tips()
        }
        this.setCancelable(false)
        setDialogMatchParent(context, this)
    }

    interface TipsOnClickListener {
        fun tips()
    }

    fun setTipsOnClickListener(tipsOnClickListener: TipsOnClickListener) {
        this.mTipsOnClickListener = tipsOnClickListener
    }

    private fun setDialogMatchParent(context: Context, dialog: Dialog) {
//        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
//        val dm = DisplayMetrics()
//        wm.defaultDisplay.getMetrics(dm)
//        val width = dm.widthPixels
//        val height = dm.heightPixels
        val layoutParams = dialog.window!!.attributes
        //这个地方也可以用ViewGroup.LayoutParams.MATCH_PARENT属性
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = layoutParams
    }

}