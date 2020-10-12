package com.biotecan.questionnaire.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import android.util.Log
import android.webkit.WebSettings
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.pre.PreApi
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_single_html.*
import kotlinx.android.synthetic.main.best_top_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class SingleHtmlActivity : BaseActivity() {

    private var html: String? = null
    private var bn: String? = null

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_single_html
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "问卷详情"
        best_top_back.setOnClickListener { onBackPressed() }
        bn = intent.getStringExtra("bn")
        val url = "${PreApi.singleHtml}$bn"

        val map = HashMap<String, String>()
        val secretToken = MyApplication.myApp!!.secretToken
        if (secretToken == null) {
            map["Authorization"] = "Basic ${MyApplication.myApp!!.baseStr}"
        } else {
            map["Authorization"] = "Bearer ${secretToken.access_token}"
        }

        single_web.loadUrl(url, map)
        val webSettings = single_web.settings
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.javaScriptEnabled = true
        webSettings.domStorageEnabled = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

    }

}