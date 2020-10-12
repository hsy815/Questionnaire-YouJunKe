package com.biotecan.questionnaire.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.Explode
import android.webkit.WebSettings
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_web.*
import kotlinx.android.synthetic.main.best_top_layout.*

class WebActivity : BaseActivity() {
    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Explode()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_web
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        val mUrl = intent.getStringExtra("mUrl")
        val mTitle = intent.getStringExtra("mTitle")
        best_top_back.setOnClickListener { onBackPressed() }
        best_top_title.text = mTitle
        web_view.loadUrl(mUrl)
        val webSettings = web_view.settings
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
    }

}