package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_replace_phone.*
import kotlinx.android.synthetic.main.best_top_layout.*

class ReplacePhoneActivity : BaseActivity() {
    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_replace_phone
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        val mUserInfo = MyApplication.myApp!!.appUserInfo!!
        best_top_title.text = "更换手机号"
        replace_phone_phone.text = mUserInfo.MobilePhone
        best_top_back.setOnClickListener { onBackPressed() }
        replace_phone_btn.setOnClickListener {
            val intent = Intent(this, ReplacePhoneCodeActivity::class.java)
            intent.putExtra("isSign", 1)
            intent.putExtra("phone", mUserInfo.MobilePhone)
            MyApplication.myApp!!.oldPhone = mUserInfo.MobilePhone
            myStartActivity(intent, this)
        }
    }

}
