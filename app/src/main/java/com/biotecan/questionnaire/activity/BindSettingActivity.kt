package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.transition.Slide
import android.view.View
import androidx.core.content.ContextCompat
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_bind_setting.*
import kotlinx.android.synthetic.main.best_top_layout.*

class BindSettingActivity : BaseActivity(), View.OnClickListener {

    private lateinit var mUserInfo: UserInfo

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_bind_setting
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        mUserInfo = MyApplication.myApp!!.appUserInfo!!
        best_top_title.text = "账号设置"
        setting_bind_phone.text = mUserInfo.MobilePhone
        best_top_back.setOnClickListener(this)
        setting_up_phone.setOnClickListener(this)
        setting_sign_wx.setOnClickListener(this)
        setting_sign_qq.setOnClickListener(this)
        isWxAndQq()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.best_top_back -> {
                onBackPressed()
            }
            R.id.setting_up_phone -> {
                myStartActivity(Intent(this, ReplacePhoneActivity::class.java), this)
            }
            R.id.setting_sign_wx -> {

            }
            R.id.setting_sign_qq -> {

            }
        }
    }

    private fun isWxAndQq() {
        if (!TextUtils.isEmpty(mUserInfo.SNS)) {
            bing_setting_wx.text = resources.getText(R.string.bind_setting)
            bing_setting_wx.setTextColor(ContextCompat.getColor(this, R.color.l_p_wx))
        } else {
            bing_setting_wx.text = resources.getText(R.string.bind_setting_no)
            bing_setting_wx.setTextColor(ContextCompat.getColor(this, R.color.replace_code_text))
        }

        if (true) {
            bing_setting_qq.text = resources.getText(R.string.bind_setting)
            bing_setting_qq.setTextColor(ContextCompat.getColor(this, R.color.l_p_wx))
        } else {
            bing_setting_qq.text = resources.getText(R.string.bind_setting_no)
            bing_setting_qq.setTextColor(ContextCompat.getColor(this, R.color.replace_code_text))
        }
    }
}
