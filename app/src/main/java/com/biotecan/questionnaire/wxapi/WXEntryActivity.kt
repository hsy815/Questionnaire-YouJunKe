package com.biotecan.questionnaire.wxapi

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.WXAccess
import com.biotecan.questionnaire.entity.WXUserInfo
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.pre.PreUser
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.SPreferences
import com.google.gson.Gson
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.wxapi
 * @创始人: hsy
 * @创建时间: 2020/4/8 15:21
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 15:21
 * @修改描述:
 */
class WXEntryActivity : BaseActivity(), IWXAPIEventHandler {
    companion object {
        val APP_ID = "wx1487058f49bfb294"
        val SECRET = "43019d8755e7383c6b575c569dae22f7"
    }

    private var mWXUserInfo: WXUserInfo? = null
    private var wxAccess: WXAccess? = null
    private var api: IWXAPI? = null
    override fun init(savedInstanceState: Bundle?) {

    }

    override fun setLayoutResourceID(): Int {
        return 0
    }

    override fun initUI() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
        api = WXAPIFactory.createWXAPI(this, APP_ID, true)
        api!!.handleIntent(intent, this)
    }

    override fun onResp(p0: BaseResp?) {
        when (p0!!.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val code = (p0 as SendAuth.Resp).code
                getAccessToken(code)
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                MyToast.makeS(this, "拒绝授权")
                finish()
            }
            BaseResp.ErrCode.ERR_USER_CANCEL -> {
                MyToast.makeS(this, "用户取消")
                finish()
            }
        }
    }

    private fun getAccessToken(code: String?) {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getWXAccess(APP_ID, SECRET, code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<WXAccess>(this) {
                    override fun onNext(t: WXAccess?) {
                        wxAccess = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        wxAccessManager()
                    }
                })
        )
    }

    private fun wxAccessManager() {
        loadingDialog.dismiss()
        if (wxAccess == null) return
        Log.e("WXEntryActivity", "access=${wxAccess!!.access_token},openId=${wxAccess!!.openid}")
        getUserInfo(wxAccess!!.access_token, wxAccess!!.openid)
    }

    private fun getUserInfo(accessToken: String, openid: String) {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getWXUserInfo(accessToken, openid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<WXUserInfo>(this) {
                    override fun onNext(t: WXUserInfo?) {
                        mWXUserInfo = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        mWXUserInfoManager()
                    }
                })
        )
    }

    private fun mWXUserInfoManager() {
        loadingDialog.dismiss()
        if (mWXUserInfo == null) return
        val sPreferences = SPreferences(this, PreUser.wxUserInfo)
        val gson = Gson()
        val data = gson.toJson(mWXUserInfo)
        sPreferences.saveSp(PreUser.wxResponseInfo, data)
        finish()
    }

    override fun onReq(p0: BaseReq?) {

    }

}