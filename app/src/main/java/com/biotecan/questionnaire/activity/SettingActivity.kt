package com.biotecan.questionnaire.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.transition.Slide
import android.view.View
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.pre.PreUser
import com.biotecan.questionnaire.util.SPreferences
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_setting.*
import kotlinx.android.synthetic.main.best_top_layout.*


class SettingActivity : BaseActivity(), View.OnClickListener {
    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_setting
    }

    @SuppressLint("SetTextI18n")
    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "设置"
        best_top_back.setOnClickListener(this)
        setting_bind.setOnClickListener(this)
        setting_up_data.setOnClickListener(this)
        setting_above.setOnClickListener(this)
        setting_sign_out.setOnClickListener(this)
        setting_version.text = "${resources.getText(R.string.setting_version)}${getVersion()}"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.best_top_back -> {
                onBackPressed()
            }
            R.id.setting_bind -> {//账号绑定设置
                myStartActivity(Intent(this, BindSettingActivity::class.java), this)
            }
            R.id.setting_up_data -> {//软件更新
                launchAppDetail("com.biotecan.questionnaire", "")
            }
            R.id.setting_above -> {//关于我们
                myStartActivity(Intent(this, AboveActivity::class.java), this)
            }
            R.id.setting_sign_out -> {//退出登录
                signOut()
            }
        }
    }

    private fun signOut() {
        val sPreferences = SPreferences(this, PreUser.spKey)
        sPreferences.saveSp(PreUser.vxKey, "")
        sPreferences.saveSp(PreUser.qqKey, "")
        MyApplication.myApp?.closeApp()
    }

    private fun getVersion(): String? {
        return try {
            val manager = this.packageManager
            val info = manager.getPackageInfo(this.packageName, 0)
            info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    /**
     * 跳转到app详情界面
     * @param appPkg App的包名
     * @param marketPkg
     * 应用商店包名 ,如果为""则由系统弹出应用商店列表供用户选择,否则调转到目标市场的应用详情界面，某些应用商店可能会失败
     */
    private fun launchAppDetail(appPkg: String, marketPkg: String?) {
        try {
            if (TextUtils.isEmpty(appPkg)) return
            val uri: Uri = Uri.parse("market://details?id=$appPkg")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            if (!TextUtils.isEmpty(marketPkg)) intent.setPackage(marketPkg)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }
}
