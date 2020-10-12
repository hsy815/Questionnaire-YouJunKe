package com.biotecan.questionnaire.util

import android.content.Context

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.util
 * @创始人: hsy
 * @创建时间: 2020/4/8 11:14
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 11:14
 * @修改描述:
 */
object DensityUtil {
    /**
     * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
     */
    fun dip2px(context: Context, dpValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale: Float = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }
}