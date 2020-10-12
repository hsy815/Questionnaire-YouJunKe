package com.biotecan.questionnaire.util

import android.content.Context

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.util
 * @创始人: hsy
 * @创建时间: 2020/5/8 15:20
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/8 15:20
 * @修改描述:
 */
object DpUtil {

    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (pxValue / scale + 0.5f).toInt()
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density;
        return (dpValue * scale + 0.5f).toInt()
    }
}