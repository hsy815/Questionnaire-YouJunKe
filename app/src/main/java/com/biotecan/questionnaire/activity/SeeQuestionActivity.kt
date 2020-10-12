package com.biotecan.questionnaire.activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Slide
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.adapter.SeeQuestionAdapter
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.FindAudience
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_see_question.*
import kotlinx.android.synthetic.main.best_top_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class SeeQuestionActivity : BaseActivity() {

    private lateinit var mSeeQuestionAdapter: SeeQuestionAdapter
    private var stateData: OptResultDTO<Boolean>? = null
    private var findAudience: OptResultDTO<List<FindAudience>>? = null
    private var mTypeKey: String? = null
    private var mSchemeInstancesBN: String? = null
    private var mUserInfoBN: String? = null

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_see_question
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "问卷"
        best_top_back.setOnClickListener { onBackPressed() }
        mTypeKey = intent.getStringExtra("TypeKey")
        mSchemeInstancesBN = intent.getStringExtra("SchemeInstancesBN")
        mUserInfoBN = intent.getStringExtra("mUserInfoBN")

        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        see_question_cyc.layoutManager = layoutManager
        mSeeQuestionAdapter = SeeQuestionAdapter(this)
        see_question_cyc.adapter = mSeeQuestionAdapter
        mSeeQuestionAdapter.setOnItemListener(object : SeeQuestionAdapter.OnItemListener {
            override fun item(findAudience: FindAudience) {
                val intent = Intent(this@SeeQuestionActivity, SingleHtmlActivity::class.java)
                intent.putExtra("bn", findAudience.BN)
                myStartActivity(intent, this@SeeQuestionActivity)
            }
        })
        see_question_btn.setOnClickListener {
            goNext()
        }
    }

    override fun onResume() {
        super.onResume()
        getState()
    }

    private fun goNext() {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("TypeKey", mTypeKey)
        startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun getState() {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription?.add(
            DataManager.getExistByAudience(mUserInfoBN!!, mTypeKey!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<Boolean>>(this) {
                    override fun onNext(t: OptResultDTO<Boolean>?) {
                        stateData = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        stateDataManager()
                    }
                })
        )
    }

    private fun stateDataManager() {
        loadingDialog.dismiss()
        if (stateData == null) {
            MyToast.makeS(this, "请求失败")
            return
        }
        if (stateData!!.OperatingStatus) {
            isButton(stateData?.DataResult!!)
        } else {
            MyToast.makeS(this, stateData?.Message)
        }
        getData()
    }

    private fun getData() {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription?.add(
            DataManager.getFindAudienceData(mSchemeInstancesBN!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<List<FindAudience>>>(this) {
                    override fun onNext(t: OptResultDTO<List<FindAudience>>?) {
                        findAudience = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        findManager()
                    }

                })
        )
    }

    private fun findManager() {
        loadingDialog.dismiss()
        if (findAudience == null) {
            MyToast.makeS(this, "请求失败")
            return
        }
        if (findAudience!!.OperatingStatus) {
            mSeeQuestionAdapter.setSeeList(findAudience?.DataResult as ArrayList<FindAudience>)
        } else {
            MyToast.makeS(this, findAudience?.Message)
        }
    }

    private fun isButton(toString: Boolean) {
        if (toString) {
            see_question_btn.background = ContextCompat.getDrawable(this, R.drawable.bind_phone_bg)
            see_question_btn.isEnabled = true
        } else {
            see_question_btn.background =
                ContextCompat.getDrawable(this, R.drawable.bind_phone_bg_un)
            see_question_btn.isEnabled = false
        }
    }

}
