package com.biotecan.questionnaire.manager

import android.content.Context
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.UserInfo
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
 * @创建时间: 2020/4/14 9:44
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/14 9:44
 * @修改描述:
 */
class LoginManager(val mContext: Context, val mOnLoginData: OnLoginData,val mLoadingDialog: LoadingDialog) {
    private var mSignIn: OptResultDTO<UserInfo>? = null

    fun requestSignIn(
        phone: String,
        subscription: CompositeSubscription
    ) {
        subscription.add(
            DataManager.getSignIn(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<UserInfo>>(mContext) {
                    override fun onNext(t: OptResultDTO<UserInfo>?) {
                        mSignIn = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        mLoadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        MyApplication.myApp!!.appUserInfo = mSignIn?.DataResult
                        mOnLoginData.loginData(mSignIn)
                    }
                })
        )
    }

    fun getFindInfo(
        bn: String,
        subscription: CompositeSubscription
    ) {
        subscription.add(
            DataManager.getFindInfo(bn)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<UserInfo>>(mContext) {
                    override fun onNext(t: OptResultDTO<UserInfo>?) {
                        mSignIn = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        mLoadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        MyApplication.myApp!!.appUserInfo = mSignIn?.DataResult
                        mOnLoginData.loginData(mSignIn)
                    }
                })
        )
    }

    interface OnLoginData {
        fun loginData(mSignIn: OptResultDTO<UserInfo>?)
    }
}