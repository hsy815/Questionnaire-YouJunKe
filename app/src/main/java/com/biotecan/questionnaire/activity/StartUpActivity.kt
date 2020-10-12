package com.biotecan.questionnaire.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.SecretToken
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.manager.LoginManager
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.pre.PreUser
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.SPreferences
import kotlinx.android.synthetic.main.activity_start_up.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class StartUpActivity : BaseActivity() {

    private var signIn: OptResultDTO<UserInfo>? = null

    private val handler = Handler()
    override fun init(savedInstanceState: Bundle?) {

    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_start_up
    }

    override fun initUI() {
        handler.postDelayed({ secretTokenManager() }, 4000)
        start_up_text.setOnClickListener { goNext() }
        getToken()
    }

    /**
     * 获取APP启动token
     */
    private fun getToken() {
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getSecretToken(MyApplication.myApp!!.getBody(MyApplication.myApp!!.grantType()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<SecretToken>(this) {
                    override fun onNext(t: SecretToken?) {
                        MyApplication.myApp!!.secretToken = t
                    }

                    override fun onCompleted() {
                        secretTokenManager()
                    }
                })
        )
    }

    /**
     * 处理启动token
     */
    private fun secretTokenManager() {
        if (MyApplication.myApp!!.secretToken == null) {
            MyToast.makeL(this, "请重启APP")
            finish()
        } else {
            val str = getLoginKey()
            if (TextUtils.isEmpty(str)) {
                goNext()
            } else {
                getSignIn(str!!)
            }
        }
    }

    /**
     * 返回保存的登陆凭证
     */
    private fun getLoginKey(): String? {
        val sPreferences = SPreferences(this, PreUser.spKey)
        val vxCode = sPreferences.getSp(PreUser.vxKey)
        val qqCode = sPreferences.getSp(PreUser.qqKey)
        return when {
            !TextUtils.isEmpty(vxCode) -> {
                vxCode
            }
            !TextUtils.isEmpty(qqCode) -> {
                qqCode
            }
            else -> {
                ""
            }
        }
    }

    private fun getSignIn(str: String) {
        subscription = CompositeSubscription()
        val loginManager = LoginManager(this, object : LoginManager.OnLoginData {
            override fun loginData(mSignIn: OptResultDTO<UserInfo>?) {
                loadingDialog.dismiss()
                signIn = mSignIn
                mSignInManager()
            }
        },loadingDialog)
        loginManager.requestSignIn(str, subscription!!)
    }

    private fun mSignInManager() {
        if (signIn != null && signIn!!.OperatingStatus) {
            goMain()
        } else {
            goNext()
        }
    }

    private fun goNext() {
        myStartActivity(Intent(this, LoginPlusActivity::class.java), this)
//        myStartActivity(Intent(this, LoginActivity::class.java), this)
        finish()
    }

    private fun goMain() {
        myStartActivity(Intent(this, MainActivity::class.java), this)
        finish()
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacksAndMessages(null)
    }

}
