package com.biotecan.questionnaire.activity

import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_above.*
import kotlinx.android.synthetic.main.best_top_layout.*

class AboveActivity : BaseActivity() {
    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_above
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "关于我们"
        best_top_back.setOnClickListener { onBackPressed() }
        above_phone_tx.text = "800-820-0608"
        above_male_tx.text = "service@biotecan.com"
    }

}
