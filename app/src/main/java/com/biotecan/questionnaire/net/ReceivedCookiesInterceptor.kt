package com.biotecan.questionnaire.net

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import rx.Observable

/**
 * @项目名: DemoApplication
 * @类位置: com.hsy.demoapplication.net
 * @创始人: hsy
 * @创建时间: 2019/3/7 14:41
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2019/3/7 14:41
 * @修改描述:
 */
class ReceivedCookiesInterceptor(val context: Context) : Interceptor {

    var sharedPreferences: SharedPreferences? = null

    init {
        sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain == null)
            Log.d("http", "Receivedchain == null")
        val response = chain.proceed(chain.request())
        if (!response.headers("set-cookie").isEmpty()) {
            val cookieBuffer = StringBuffer()
            Observable.from(response.headers("set-cookie"))
                .map { s ->
                    val cookieArray = s.split(";")
                    return@map cookieArray
                }
                .subscribe { cookie ->
                    cookieBuffer.append(cookie).append(";")
                }
            Log.d("http", "Receivedchain,$cookieBuffer")
            val editor = sharedPreferences!!.edit()
            editor.putString("cookie", cookieBuffer.toString())
            editor.apply()
        }
        return response
    }
}