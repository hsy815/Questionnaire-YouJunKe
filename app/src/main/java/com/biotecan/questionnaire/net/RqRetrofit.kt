package com.biotecan.questionnaire.net

import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.pre.PreApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.net
 * @创始人: hsy
 * @创建时间: 2020/4/8 13:49
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 13:49
 * @修改描述:
 */
object RqRetrofit {

    fun rRetrofit(): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(AddToken())
//            .addInterceptor(ReceivedCookiesInterceptor(MyApplication.myApp!!.applicationContext))
//            .addInterceptor(AddCookiesInterceptor(MyApplication.myApp!!.applicationContext, ""))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(PreApi.url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }

    fun rRetrofit(url: String): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ReceivedCookiesInterceptor(MyApplication.myApp!!.applicationContext))
            .addInterceptor(AddCookiesInterceptor(MyApplication.myApp!!.applicationContext, ""))
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .build()
        return retrofit.create(ApiService::class.java)
    }
}