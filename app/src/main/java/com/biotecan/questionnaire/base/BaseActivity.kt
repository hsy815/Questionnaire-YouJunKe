package com.biotecan.questionnaire.base

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.view.LoadingDialog
import rx.subscriptions.CompositeSubscription

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.base
 * @创始人: hsy
 * @创建时间: 2020/4/3 14:53
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/3 14:53
 * @修改描述:
 */
abstract class BaseActivity : AppCompatActivity() {

    internal lateinit var loadingDialog: LoadingDialog

    var subscription: CompositeSubscription? = null

    /**
     * 初始化之前的操作
     */
    abstract fun init(savedInstanceState: Bundle?)

    /**
     * 设置ContentView
     */
    abstract fun setLayoutResourceID(): Int

    /**
     * 处理UI操作
     */
    abstract fun initUI()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init(savedInstanceState)
        if (0 != setLayoutResourceID())
            setContentView(setLayoutResourceID())

        loadingDialog = LoadingDialog(this)
        MyApplication.myApp?.activityList?.add(this)
        initUI()
    }

    /**
     * 使用动画跳转
     */
    fun myStartActivity(intent: Intent?, activity: Activity) {
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle())
    }

    fun myStartActivity(intent: Intent?) {
        startActivity(intent)
    }

    override fun onStop() {
        super.onStop()
        if (subscription != null && subscription!!.hasSubscriptions()) {
            subscription!!.unsubscribe()
        }
    }

}