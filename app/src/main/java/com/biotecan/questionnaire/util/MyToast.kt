package com.biotecan.questionnaire.util

import android.content.Context
import android.text.TextUtils
import android.widget.Toast

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.util
 * @创始人: hsy
 * @创建时间: 2020/4/7 17:05
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/7 17:05
 * @修改描述:
 */
object MyToast {

    fun makeS(context: Context, string: String?) {
        if (TextUtils.isEmpty(string)) return
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    fun makeL(context: Context, string: String?) {
        if (TextUtils.isEmpty(string)) return
        Toast.makeText(context, string, Toast.LENGTH_LONG).show()
    }
}