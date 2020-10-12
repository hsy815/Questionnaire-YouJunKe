package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.transition.Slide
import androidx.recyclerview.widget.LinearLayoutManager
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.adapter.IdentityAdapter
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.Disease
import com.biotecan.questionnaire.entity.OptResultDTO
import com.biotecan.questionnaire.entity.SchemeInstances
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import kotlinx.android.synthetic.main.activity_open_identity.*
import kotlinx.android.synthetic.main.best_top_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class OpenIdentityActivity : BaseActivity() {

    private var mUserIdentity: List<SchemeInstances>? = null
    private var diseaseList: OptResultDTO<List<Disease>>? = null
    private lateinit var selectAdapter: IdentityAdapter
    private var editDiseaseList: ArrayList<Disease> = ArrayList()

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Slide()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_open_identity
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "开通身份"
        best_top_back.setOnClickListener { onBackPressed() }
        MyApplication.myApp?.mOpenIdentityActivity = this
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        identity_cyc.layoutManager = layoutManager
        selectAdapter = IdentityAdapter(this)
        identity_cyc.adapter = selectAdapter
        selectAdapter.setOnItemClick(object : IdentityAdapter.OnItemClick {
            override fun item(disease: Disease) {
                goNext(disease)
            }
        })
        getData()
    }

    private fun goNext(disease: Disease) {
        val userInfo = MyApplication.myApp?.appUserInfo
        if (TextUtils.isEmpty(userInfo?.Name) ||
            0 == userInfo?.Gender
        ) {
            myStartActivity(Intent(this, UserSettingActivity::class.java), this)
            return
        }
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("TypeKey", disease.BN)
        myStartActivity(intent, this)
    }

    private fun getData() {
        loadingDialog.show()
        mUserIdentity = MyApplication.myApp?.appUserInfo?.SchemeInstances
        subscription = CompositeSubscription()
        subscription!!.add(
            DataManager.getDiseaseList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<OptResultDTO<List<Disease>>>(this) {
                    override fun onNext(t: OptResultDTO<List<Disease>>?) {
                        diseaseList = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                    }

                    override fun onCompleted() {
                        diseaseManager()
                    }
                })
        )
    }

    private fun diseaseManager() {
        loadingDialog.dismiss()
        if (diseaseList == null) {
            MyToast.makeS(this, "请求失败")
            return
        }
        if (diseaseList!!.OperatingStatus) {
            editList(diseaseList!!.DataResult)
            selectAdapter.setData(editDiseaseList)
        } else {
            MyToast.makeS(this, diseaseList!!.Message)
        }
    }

    private fun editList(dataResult: List<Disease>?) {
        if (dataResult == null) return
        if (mUserIdentity == null) return

        for (disease: Disease in dataResult) {
            for (identity: SchemeInstances in mUserIdentity!!) {
                if (disease.BN == identity.TypeKey) {
                    disease.isSelect = true
                }
            }
            editDiseaseList.add(disease)
        }
    }
}
