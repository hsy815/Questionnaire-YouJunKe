package com.biotecan.questionnaire.manager

import android.content.Context
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.view.LoadingDialog
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.manager
 * @创始人: hsy
 * @创建时间: 2020/5/26 13:15
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/26 13:15
 * @修改描述:
 */
class SendCodeManager(
    val mContext: Context,
    val mOnSendData: OnSendData,
    val mLoadingDialog: LoadingDialog
) {

    private var sendCode: OptResultDTO<String>? = null

    fun getSendCode(phone: String, subscription: CompositeSubscription) {
        subscription.add(
            DataManager.getSendCode(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<String>>(mContext) {
                    override fun onNext(t: OptResultDTO<String>?) {
                        sendCode = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        mLoadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        mOnSendData.data(sendCode)
                    }
                })
        )
    }

    interface OnSendData {
        fun data(sendCode: OptResultDTO<String>?)
    }
}