package com.biotecan.questionnaire.activity

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.transition.Slide
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.adapter.QuestionFragmentAdapter
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.*
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import com.biotecan.questionnaire.view.TipsDialog
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.best_top_layout.*
import kotlinx.android.synthetic.main.question_top.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class QuestionActivity : BaseActivity() {

    private var submitResponse: OptResultDTO<String>? = null//提交采集表单结果反馈
    private lateinit var userInfo: UserInfo
    private var mTypeKey = ""
    var questionPresets: List<Presets>? = null//请求回来的问卷题目列表
    var questionSubmitItem: MutableList<SubmitItem> = ArrayList()//每一个问卷问题的答案列表
    var questionMap: HashMap<String, ArrayList<SubmitItem>?> = HashMap()//多选问题答案列表
    private var questionData: QuestionResult? = null//请求回来的问卷集合
    private var optResult: OptResultDTO<QuestionResult>? = null//请求回来的未处理问卷集合
    private var questionSize = 0//问题总数

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_question
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "在线调差"
        best_top_right.text = "提交"
        best_top_right.visibility = View.GONE
        best_top_right.setTextColor(ContextCompat.getColor(this, R.color.l_p_wx))
        best_top_back.setOnClickListener { onBackPressed() }
        best_top_right.setOnClickListener {
            submitDataManager()
        }
        userInfo = MyApplication.myApp!!.appUserInfo!!
        mTypeKey = intent.getStringExtra("TypeKey")
        question_vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                question_position.text = (position + 1).toString()
                if ((position + 1) == questionSize) {
                    best_top_right.visibility = View.VISIBLE
                } else {
                    best_top_right.visibility = View.GONE
                }
            }
        })
        initData()
    }

    private fun showTips() {
        val tipsDialog = TipsDialog(this)
        tipsDialog.setTipsOnClickListener(
            object : TipsDialog.TipsOnClickListener {
                override fun tips() {
                    tipsDialog.dismiss()
                }
            }
        )
        tipsDialog.show()
    }

    private fun submitDataManager() {
        for (frag: Fragment in supportFragmentManager.fragments) {
            frag.onPause()
        }

        val submitList = ArrayList<SubmitItem>()
        for (i: Int in questionPresets!!.indices) {
            when (questionPresets!![i].Type) {
                0, 3 -> {//单选及附加
                    if (TextUtils.isEmpty(questionSubmitItem[i].Value)) {
                        MyToast.makeL(this, "第${i + 1}题 ${questionPresets!![i].Question} 未完成作答")
                        return
                    }
                    submitList.add(questionSubmitItem[i])
                }
                else -> {//1、多选 2、多选及附加填入
                    val checkList = questionMap[questionPresets!![i].BN]
                    if (null == checkList) {
                        MyToast.makeL(this, "第${i + 1}题 ${questionPresets!![i].Question} 未完成作答")
                        return
                    }
                    submitList.addAll(checkList)
                }
            }
        }

        setSubmitData(submitList)
    }

    /**
     * 提交数据整合
     */
    private fun setSubmitData(submitList: ArrayList<SubmitItem>) {
        loadingDialog.show()
        val submitMap: HashMap<String, String?> = HashMap()
        submitMap["ContentKey"] = questionData!!.BN
        submitMap["AudienceKey"] = userInfo.BN
        //折磨人的数据格式   啊啊啊啊啊啊啊啊啊啊~~~
        for (i: Int in 0 until submitList.size) {
            val sub = submitList[i]
            submitMap["Items[$i].ItemKey"] = sub.ItemKey
            submitMap["Items[$i].ValueKey"] = sub.ValueKey
            submitMap["Items[$i].Value"] = sub.Value
            submitMap["Items[$i].PlusValue"] = sub.PlusValue
        }
        submitData(submitMap)
    }

    /**
     * 获取问卷问题
     */
    private fun initData() {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getDataRecruitFormApi(userInfo.BN, mTypeKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<QuestionResult>>(this) {
                    override fun onNext(t: OptResultDTO<QuestionResult>?) {
                        optResult = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.show()
                    }

                    override fun onCompleted() {
                        optResultManager()
                    }
                })
        )
    }

    /**
     * 请求回来的问卷问题集合
     */
    private fun optResultManager() {
        loadingDialog.dismiss()
        if (optResult == null || !optResult!!.OperatingStatus) {
            MyToast.makeS(this, optResult?.Message)
            question_position.text = "0"
            question_count.text = "/0"
            return
        } else {
            questionData = optResult!!.DataResult
            questionPresets = questionData!!.Presets
            question_position.text = "1"
            questionSize = questionPresets!!.size
            question_count.text = "/${questionSize}"
            showTips()
            initFragment()
        }
    }

    private fun initFragment() {
        val questionAdapter = QuestionFragmentAdapter(supportFragmentManager, questionPresets!!)
        question_vp.adapter = questionAdapter
    }

    /**
     * 提交问卷答案
     */
    private fun submitData(submitMap: HashMap<String, String?>) {
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getDataSubmit(submitMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<String>>(this) {
                    override fun onNext(t: OptResultDTO<String>?) {
                        submitResponse = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        submitResponseManager()
                    }
                })
        )
    }

    private fun submitResponseManager() {
        loadingDialog.dismiss()
        if (submitResponse == null) {
            MyToast.makeS(this, "提交失败请重试")
            return
        }
        if (submitResponse!!.OperatingStatus) {
            MyToast.makeS(this, "提交成功")
            setResult(200)
            MyApplication.myApp?.mOpenIdentityActivity?.onBackPressed()
            onBackPressed()
        } else {
            MyToast.makeS(this, submitResponse!!.Message)
        }
    }
}
