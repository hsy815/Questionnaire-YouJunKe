package com.biotecan.questionnaire.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.util
 * @创始人: hsy
 * @创建时间: 2020/4/29 16:54
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/29 16:54
 * @修改描述:
 */
object BitmapOrBase64 {

    fun bitmap2Base64(bitmap: Bitmap?): String? {
        var result: String? = null
        var baos: ByteArrayOutputStream? = null
        try {
            baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            baos.flush()
            baos.close()
            val bitmapBytes = baos.toByteArray()
            result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            if (baos != null) {
                baos.flush()
                baos.close()
            }
        }
        return result
    }

    fun base64ToBitmap(base64: String): Bitmap {
        val mBase64 = Base64.decode(base64, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(mBase64, 0, mBase64.size)
    }
}