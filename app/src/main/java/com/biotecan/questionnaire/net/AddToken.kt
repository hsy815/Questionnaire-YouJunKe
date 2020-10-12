package com.biotecan.questionnaire.net

import com.biotecan.questionnaire.MyApplication
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.net
 * @创始人: hsy
 * @创建时间: 2020/4/13 14:08
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/13 14:08
 * @修改描述:
 */
class AddToken() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val headers = Headers.Builder()
        val secretToken = MyApplication.myApp!!.secretToken
        if (secretToken == null) {
            headers.addUnsafeNonAscii("Authorization", "Basic ${MyApplication.myApp!!.baseStr}")
        } else {
            headers.addUnsafeNonAscii(
                "Authorization",
                "Bearer ${secretToken.access_token}"
            )
        }
        val requestBuilder: Request.Builder = original.newBuilder()
            .headers(headers.build())
        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}