package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Slide
import androidx.core.content.ContextCompat
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_bind_new_phone.*
import kotlinx.android.synthetic.main.best_top_layout.*

class BindNewPhoneActivity : BaseActivity() {

    private lateinit var newPhone: String

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_bind_new_phone
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        MyApplication.myApp?.mBindNewPhoneActivity = this
        best_top_back.setOnClickListener { onBackPressed() }
        bing_phone_tx.isEnabled = false
        bing_phone_tx.setOnClickListener {
            MyApplication.myApp?.mReplacePhoneCodeActivity?.onBackPressed()
            val intent = Intent(this, ReplacePhoneCodeActivity::class.java)
            intent.putExtra("isSign", 2)
            intent.putExtra("phone", newPhone)
            myStartActivity(intent, this)
        }
        bing_phone_ed.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                isButton(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })
    }

    private fun isButton(toString: String) {
        if (11 == toString.length) {
            bing_phone_tx.background = ContextCompat.getDrawable(this, R.drawable.bind_phone_bg)
            bing_phone_tx.isEnabled = true
            newPhone = toString
        } else {
            bing_phone_tx.background = ContextCompat.getDrawable(this, R.drawable.bind_phone_bg_un)
            bing_phone_tx.isEnabled = false
        }
    }

}
