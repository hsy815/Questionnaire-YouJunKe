package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.transition.Slide
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.manager.LoginManager
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_login.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class LoginActivity : BaseActivity() {

    private var mRegister: OptResultDTO<UserInfo>? = null
    private var signIn: OptResultDTO<UserInfo>? = null
    private var sendCode: OptResultDTO<String>? = null
    private lateinit var vxCode :String
    private var rsSendCode: String? = ""
    private var strPhone: String = ""
    private var isSend = true
    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onFinish() {
            login_text_code.setText(R.string.login_get_code)
            isSend = true
        }

        override fun onTick(millisUntilFinished: Long) {
            login_text_code.text = "重新发送(${millisUntilFinished / 1000}S)"
            isSend = false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_login
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        vxCode = intent.getStringExtra("vxCode")
        login_edit_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s!!.length == 4) {
                    codeManager(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        login_text_code.setOnClickListener {
            if (isSend) {
                timer.start()
                getSendCode()
            }
        }
    }

    /**
     * 发起验证码请求
     */
    private fun getSendCode() {
        strPhone = login_edit_phone.text.toString()
        if (TextUtils.isEmpty(strPhone)) {
            MyToast.makeS(this, "请先输入手机号")
            return
        }

        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getSendCode(strPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<String>>(this) {
                    override fun onNext(t: OptResultDTO<String>?) {
                        sendCode = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        sendCodeManager()
                    }
                })
        )
    }

    private fun sendCodeManager() {
        loadingDialog.dismiss()
        if (sendCode != null && sendCode!!.OperatingStatus) {
            rsSendCode = sendCode!!.DataResult
        } else {
            MyToast.makeL(this, "请倒计时结束后重试")
        }
    }

    /**
     * 输入验证 发起注册
     */
    private fun codeManager(editable: String?) {
        if (editable.equals(rsSendCode)) {
            getRegister()
        } else {
            MyToast.makeS(this, "请输入正确的验证码")
        }
    }

    /**
     * 注册请求
     */
    private fun getRegister() {
        loadingDialog.show()
        val map = HashMap<String, String>()
        map["MobilePhone"] = strPhone
        map["SNS"] = vxCode
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getRegister(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<UserInfo>>(this) {
                    override fun onNext(t: OptResultDTO<UserInfo>?) {
                        mRegister = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        mRegisterManager()
                    }
                })
        )
    }

    /**
     * 注册处理
     */
    private fun mRegisterManager() {
        loadingDialog.dismiss()
        if (mRegister != null && mRegister!!.OperatingStatus) {
            goSignIn()
        } else {
            MyToast.makeS(this, "注册失败，请重试")
        }
    }

    /**
     * 注册后登录
     */
    private fun goSignIn() {
        subscription = CompositeSubscription()
        val loginManager = LoginManager(this, object : LoginManager.OnLoginData {
            override fun loginData(mSignIn: OptResultDTO<UserInfo>?) {
                loadingDialog.dismiss()
                signIn = mSignIn
                mSignInManager()
            }
        }, loadingDialog)
        loginManager.requestSignIn(strPhone, subscription!!)
    }

    /**
     * 注册登录后处理
     */
    private fun mSignInManager() {
        if (signIn != null && signIn!!.OperatingStatus) {
            goNext()
        } else {
            MyToast.makeL(this, "登录失败，请退出APP重试")
        }
    }

    private fun goNext() {
        myStartActivity(Intent(this, MainActivity::class.java), this)
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
