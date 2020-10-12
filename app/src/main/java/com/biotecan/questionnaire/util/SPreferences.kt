package com.biotecan.questionnaire.util

import android.content.Context
import android.content.SharedPreferences

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.util
 * @创始人: hsy
 * @创建时间: 2020/5/25 13:18
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/25 13:18
 * @修改描述:
 */
class SPreferences(context: Context, spKey: String) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(spKey, 0)

    fun saveSp(saveKey: String, saveValue: String) {
        val sp = sharedPreferences.edit()
        sp.putString(saveKey, saveValue)
        sp.apply()
    }

    fun getSp(saveKey: String): String? {
        return sharedPreferences.getString(saveKey, "")
    }

    fun clear() {
        val sp = sharedPreferences.edit()
        sp.clear()
        sp.apply()
    }
}