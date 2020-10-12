package com.biotecan.questionnaire.net

import android.content.Context
import com.biotecan.questionnaire.util.MyToast
import rx.Subscriber

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.net
 * @创始人: hsy
 * @创建时间: 2020/4/8 14:04
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 14:04
 * @修改描述:
 */
abstract class MySubscriber<T>(private val context: Context) : Subscriber<T>() {

    override fun onStart() {
        super.onStart()
        if (!NetWorkUtil.isNetworkAvailable(context)) {
            MyToast.makeS(context, "老大，没网络呀，去看看您的网络设置吧")
            if (!isUnsubscribed) {
                unsubscribe()
            }
        }
    }

    override fun onError(e: Throwable?) {
        ThrowableManager.errorException(context, e)
    }
}