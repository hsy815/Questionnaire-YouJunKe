package com.biotecan.questionnaire.util

import java.text.SimpleDateFormat

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.util
 * @创始人: hsy
 * @创建时间: 2020/5/8 11:13
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/8 11:13
 * @修改描述:
 */
object DateAndString {

     fun getDate2(str: String): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd")
            val date = formatter.parse(str)
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }

     fun getDate2HH(str: String): String {
        return try {
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val date = formatter.parse(str)
            formatter.format(date)
        } catch (e: Exception) {
            ""
        }
    }
}