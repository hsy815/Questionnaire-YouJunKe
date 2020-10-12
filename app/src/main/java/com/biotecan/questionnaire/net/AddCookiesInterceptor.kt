package com.biotecan.questionnaire.net

import android.content.Context
import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import rx.Observable
import java.io.IOException

/**
 * @项目名: DemoApplication
 * @类位置: com.hsy.demoapplication.net
 * @创始人: hsy
 * @创建时间: 2019/3/7 14:03
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2019/3/7 14:03
 * @修改描述:
 */
class AddCookiesInterceptor(private val context: Context, private val lang: String?) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        val sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE)
        Observable.just(sharedPreferences.getString("cookie", ""))
            .subscribe { cookie ->
                var cookie = cookie
                if (cookie!!.contains("lang=ch")) {
                    cookie = cookie.replace("lang=ch", "lang=$lang")
                }
                if (cookie.contains("lang=en")) {
                    cookie = cookie.replace("lang=en", "lang=$lang")
                }
                //添加cookie
                Log.d("http", "AddCookiesInterceptor$cookie")
                builder.addHeader("cookie", cookie)
            }
        return chain.proceed(builder.build())
    }
}