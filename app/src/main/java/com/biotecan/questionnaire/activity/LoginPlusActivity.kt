package com.biotecan.questionnaire.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.transition.Slide
import android.view.View
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.entity.WXUserInfo
import com.biotecan.questionnaire.manager.LoginManager
import com.biotecan.questionnaire.pre.PreUser
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.SPreferences
import com.biotecan.questionnaire.wxapi.WXEntryActivity
import com.google.gson.Gson
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import kotlinx.android.synthetic.main.activity_login_plus.*
import rx.subscriptions.CompositeSubscription

class LoginPlusActivity : BaseActivity(), View.OnClickListener {

    private var signIn: OptResultDTO<UserInfo>? = null
    private var api: IWXAPI? = null

    private var vxCode = ""
    private var qqCode = ""
    private var sex = 0

    override fun init(savedInstanceState: Bundle?) {
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_login_plus
    }

    override fun initUI() {
        login_plus_wx.setOnClickListener(this)
        login_plus_qq.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        val sPreferences = SPreferences(this, PreUser.wxUserInfo)
        val responseInfo = sPreferences.getSp(PreUser.wxResponseInfo)
        if (responseInfo != null) {
            val gson = Gson()
            val mWXUserInfo = gson.fromJson(responseInfo, WXUserInfo::class.java)
            sPreferences.clear()
            goLoginPlus(mWXUserInfo)
        }
    }

    /**
     * 微信登录返回处理
     */
    private fun goLoginPlus(mWXUserInfo: WXUserInfo?) {
        if (mWXUserInfo == null) {
//            MyToast.makeS(this, "请重新使用微信登录")
            return
        }
        vxCode = mWXUserInfo.openid
        sex = mWXUserInfo.sex
        getSignIn(mWXUserInfo)
    }

    /**
     * 要删除
     * 测试使用
     */
    private fun getTestSignIn() {
        subscription = CompositeSubscription()
        val loginManager = LoginManager(this, object : LoginManager.OnLoginData {
            override fun loginData(mSignIn: OptResultDTO<UserInfo>?) {
                loadingDialog.dismiss()
                signIn = mSignIn
                mSignInManager()
            }
        }, loadingDialog)
        loginManager.requestSignIn("13262925689", subscription!!)
    }

    /**
     * 登录
     */
    private fun getSignIn(mWXUserInfo: WXUserInfo) {
        subscription = CompositeSubscription()
        val loginManager = LoginManager(this, object : LoginManager.OnLoginData {
            override fun loginData(mSignIn: OptResultDTO<UserInfo>?) {
                loadingDialog.dismiss()
                signIn = mSignIn
                mSignInManager()
            }
        }, loadingDialog)
        loginManager.requestSignIn(mWXUserInfo.openid, subscription!!)
    }

    /**
     * 登录处理
     */
    private fun mSignInManager() {
        if (signIn != null && signIn!!.OperatingStatus) {
            saveLoginKey()
            goMain()
        } else {
            goNext()
        }
        onBackPressed()
    }

    /**
     * 登录后保存登录凭证
     */
    private fun saveLoginKey() {
        val sPreferences = SPreferences(this, PreUser.spKey)
        sPreferences.saveSp(PreUser.vxKey, vxCode)
        sPreferences.saveSp(PreUser.qqKey, qqCode)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.login_plus_wx -> {
                regToWx()
            }
            R.id.login_plus_qq -> {
//                getTestSignIn()
            }
        }
    }

    private fun goNext() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("vxCode", vxCode)
        myStartActivity(intent, this)
    }

    private fun goMain() {
        myStartActivity(Intent(this, MainActivity::class.java), this)
    }

    /**
     * 微信登录
     */
    private fun regToWx() {
        api = WXAPIFactory.createWXAPI(this, WXEntryActivity.APP_ID, true)
        if (api!!.isWXAppInstalled) {
            api!!.registerApp(WXEntryActivity.APP_ID)
            registerReceiver(object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    api!!.registerApp("${WXEntryActivity.APP_ID}+1")
                }
            }, IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP))
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            api!!.sendReq(req)
        } else {
            MyToast.makeS(this, "请安装微信客户端")
        }
    }
}
