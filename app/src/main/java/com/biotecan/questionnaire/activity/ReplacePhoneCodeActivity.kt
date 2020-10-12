package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import androidx.core.content.ContextCompat
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.ChangePhone
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.manager.SendCodeManager
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_replace_phone_code.*
import kotlinx.android.synthetic.main.best_top_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class ReplacePhoneCodeActivity : BaseActivity() {

    private var changeData: OptResultDTO<UserInfo>? = null
    private var codeStr: String? = null
    private var isSign = 0
    private var mPhone: String? = ""
    private var isSend = true
    private val timer = object : CountDownTimer(60000, 1000) {
        override fun onFinish() {
            replace_send_code.setText(R.string.login_get_code)
            replace_send_code.setTextColor(
                ContextCompat.getColor(
                    this@ReplacePhoneCodeActivity,
                    R.color.l_p_wx
                )
            )
            isSend = true
        }

        override fun onTick(millisUntilFinished: Long) {
            replace_send_code.text = "重新发送\n(${millisUntilFinished / 1000}S)"
            replace_send_code.setTextColor(
                ContextCompat.getColor(
                    this@ReplacePhoneCodeActivity,
                    R.color.l_p_wx_50
                )
            )
            isSend = false
        }
    }

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_replace_phone_code
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        MyApplication.myApp?.mReplacePhoneCodeActivity = this
        best_top_back.setOnClickListener { onBackPressed() }
        replace_phone_code.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isCode(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
        replace_send_code.setOnClickListener {
            if (isSend) {
                timer.start()
                sendCode()
            }
        }

        isSign = intent.getIntExtra("isSign", 0)
        mPhone = intent.getStringExtra("phone")
        replace_send_phone.text = "已发送至手机$mPhone"

        sendCode()
    }

    /**
     * 发送验证码
     */
    private fun sendCode() {
        timer.start()
        subscription = CompositeSubscription()
        val sendCodeManager = SendCodeManager(this, object : SendCodeManager.OnSendData {
            override fun data(sendCode: OptResultDTO<String>?) {
                loadingDialog.dismiss()
                sendCodeManager(sendCode)
            }
        }, loadingDialog)
        sendCodeManager.getSendCode(mPhone!!, subscription!!)
    }

    /**
     * 验证码处理
     */
    private fun sendCodeManager(sendCode: OptResultDTO<String>?) {
        if (sendCode == null) {
            MyToast.makeS(this, "发送失败，请重试")
//            resetTimer()
            return
        }
        if (sendCode.OperatingStatus) {
            codeStr = sendCode.DataResult
        } else {
            MyToast.makeS(this, sendCode.DataResult)
//            resetTimer()
        }
    }

    /**
     * 置回发送验证码
     */
    private fun resetTimer() {
        timer.cancel()
        replace_send_code.setText(R.string.login_get_code)
        replace_send_code.setTextColor(
            ContextCompat.getColor(
                this@ReplacePhoneCodeActivity,
                R.color.l_p_wx
            )
        )
        isSend = true
    }

    /**
     * 按条件处理验证
     */
    private fun isCode(string: String) {
        if (4 == string.length) {
            when (isSign) {
                1 -> {//代表原始手机号发送验证码
                    if (codeEquals(string)) {
                        myStartActivity(Intent(this, BindNewPhoneActivity::class.java), this)
                    } else {
                        MyToast.makeS(this, "请输入正确的验证码")
                    }
                }
                2 -> {//代表新手机号发送验证码
                    if (codeEquals(string)) {
                        changePhone()
                    } else {
                        MyToast.makeS(this, "请输入正确的验证码")
                    }
                }
            }
        }
    }

    private fun changePhone() {
        loadingDialog.show()
        val changePhone = ChangePhone(
            MyApplication.myApp?.appUserInfo?.BN!!,
            MyApplication.myApp?.oldPhone!!,
            mPhone!!
        )
        subscription = CompositeSubscription()
        subscription?.add(
            DataManager.changeBindPhone(changePhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<UserInfo>>(this) {
                    override fun onNext(t: OptResultDTO<UserInfo>?) {
                        changeData = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        changeDataManager()
                    }
                })
        )
    }

    private fun changeDataManager() {
        loadingDialog.dismiss()
        if (changeData == null) {
            MyToast.makeS(this, "更改失败")
            return
        }
        if (changeData!!.OperatingStatus) {
            MyApplication.myApp!!.appUserInfo = changeData?.DataResult
            MyApplication.myApp?.mBindNewPhoneActivity?.onBackPressed()
            onBackPressed()
        } else {
            MyToast.makeS(this, changeData?.Message)
        }
    }

    /**
     * 判断验证码是否正确
     */
    private fun codeEquals(string: String?): Boolean {
        return (string.equals(codeStr))
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.cancel()
    }
}
