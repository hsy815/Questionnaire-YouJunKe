package com.biotecan.questionnaire.activity

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.transition.Explode
import android.view.KeyEvent
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.biotecan.questionnaire.MyApplication
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.adapter.MainButtonAdapter
import com.biotecan.questionnaire.adapter.MainNewAdapter
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.*
import com.biotecan.questionnaire.manager.LoginManager
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.pre.PreApi
import com.biotecan.questionnaire.util.BitmapOrBase64
import com.biotecan.questionnaire.util.MyToast
import com.google.android.material.appbar.AppBarLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_scrolling.*
import kotlinx.android.synthetic.main.main_input_layout.*
import kotlinx.android.synthetic.main.main_lin_title.*
import kotlinx.android.synthetic.main.main_toolbar_close.*
import kotlinx.android.synthetic.main.main_toolbar_open.*
import kotlinx.android.synthetic.main.main_toolbar_open_bg.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription
import kotlin.math.abs

class MainActivity : BaseActivity(), MainButtonAdapter.OnItemListener,
    AppBarLayout.OnOffsetChangedListener, MainNewAdapter.OnItemListener, View.OnClickListener {

    private var newsList: NewsDTO<List<NewsList>>? = null
    private var exitTime: Long = 0
    private val USER_SETTING_ACTIVITY: Int = 100
    private var mSchemeInstances: ArrayList<SchemeInstances>? = null
    private lateinit var mainButtonAdapter: MainButtonAdapter
    private lateinit var mainNewAdapter: MainNewAdapter
    private var mUserInfo: UserInfo? = null
    override fun init(savedInstanceState: Bundle?) {
        window.enterTransition = Explode()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_main
    }

    override fun initUI() {
        setSupportActionBar(main_toolbar)

        main_app_bar.addOnOffsetChangedListener(this)//头部滑动监听
        main_input_text.setOnClickListener(this)//开通问卷按钮
        main_input_text_lin.setOnClickListener(this)//开通问卷按钮
        main_input_img.setOnClickListener(this)//开通问卷按钮
        toolbar_open_head.setOnClickListener(this)//展开页面头像
        toolbar_open_personal.setOnClickListener(this)//展开页面个人信息
        toolbar_open_nickname.setOnClickListener(this)//展开页面昵称
        toolbar_open_mailbox.setOnClickListener(this)//展开页面邮箱
        toolbar_open_setting.setOnClickListener(this)//展开页面设置
        toolbar_close_head.setOnClickListener(this)//折叠页面头像
        toolbar_close_mailbox.setOnClickListener(this)//折叠页面邮箱
        toolbar_close_setting.setOnClickListener(this)//折叠页面设置
        main_news_list.setOnClickListener(this)//新闻资讯 查看更多


        //页面button列表
        val layoutManager = GridLayoutManager(this, 4)
        main_button_recycler.layoutManager = layoutManager
        mainButtonAdapter = MainButtonAdapter(this, this)
        main_button_recycler.adapter = mainButtonAdapter

        //新闻列表
        val layoutManager2 = LinearLayoutManager(this)
        layoutManager2.orientation = LinearLayoutManager.VERTICAL
        main_new_recycler.layoutManager = layoutManager2
        mainNewAdapter = MainNewAdapter(this, 3)
        main_new_recycler.adapter = mainNewAdapter
        mainNewAdapter.setOnItemListener(this)

        initData()
        getNewsList()
    }

    private fun initData() {
        mUserInfo = MyApplication.myApp?.appUserInfo
        mSchemeInstances = mUserInfo?.SchemeInstances as ArrayList<SchemeInstances>?
        if (mUserInfo != null) {
            toolbar_open_nickname.text = mUserInfo?.Name//展开页面昵称
            toolbar_open_question.text = "2"//展开页面问卷
            toolbar_open_test.text = "2"//展开页面测试
            toolbar_open_donor.text = "2"//展开页面供体
            val headStr = mUserInfo?.HeadPortrait
            if (TextUtils.isEmpty(headStr)) {
                toolbar_open_head.setImageResource(R.mipmap.head)
                toolbar_close_head.setImageResource(R.mipmap.head)
            } else {
                val bitmap = BitmapOrBase64.base64ToBitmap(headStr!!)
                toolbar_open_head.setImageBitmap(bitmap)
                toolbar_close_head.setImageBitmap(bitmap)
            }
        }
        if (mSchemeInstances != null && mSchemeInstances?.size!! > 0) {
            //按条件显示与否
            main_button_view.visibility = View.VISIBLE
            main_button_recycler.visibility = View.VISIBLE
            main_input_img.visibility = View.VISIBLE
            main_input_text.visibility = View.GONE
            mainButtonAdapter.setButtonList(mSchemeInstances!!)
        } else {
            main_button_view.visibility = View.GONE
            main_button_recycler.visibility = View.GONE
            main_input_img.visibility = View.GONE
            main_input_text.visibility = View.VISIBLE
        }
    }

    /**
     * 本页面所有点击
     */
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.toolbar_open_head,
            R.id.toolbar_open_personal,
            R.id.toolbar_open_nickname,
            R.id.toolbar_close_head -> {
                goUserSettingActivity()
            }
            R.id.toolbar_open_mailbox,
            R.id.toolbar_close_mailbox -> {

            }
            R.id.toolbar_open_setting,
            R.id.toolbar_close_setting -> {
                myStartActivity(Intent(this, SettingActivity::class.java), this)
            }
            R.id.main_input_text_lin,
            R.id.main_input_img,
            R.id.main_input_text -> {
                myStartActivity(Intent(this, OpenIdentityActivity::class.java), this)
            }
            R.id.main_news_list -> {
                myStartActivity(Intent(this, NewsListActivity::class.java), this)
            }
        }
    }

    /**
     * 跳转至用户信息修改页面
     */
    private fun goUserSettingActivity() {
        startActivityForResult(
            Intent(this, UserSettingActivity::class.java),
            USER_SETTING_ACTIVITY,
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    private fun getFindInfo() {
        subscription = CompositeSubscription()
        val loginManager = LoginManager(this, object : LoginManager.OnLoginData {
            override fun loginData(mSignIn: OptResultDTO<UserInfo>?) {
                loadingDialog.dismiss()
                initData()
            }
        }, loadingDialog)
        loginManager.getFindInfo(mUserInfo?.BN!!, subscription!!)
    }

    /**
     * 按钮列表点击事件
     */
    override fun mButton(mainButton: SchemeInstances) {
        val intent = Intent(this, SeeQuestionActivity::class.java)
        intent.putExtra("TypeKey", mainButton.TypeKey)
        intent.putExtra("SchemeInstancesBN", mainButton.BN)
        intent.putExtra("mUserInfoBN", mUserInfo?.BN)
        startActivity(
            intent,
            ActivityOptions.makeSceneTransitionAnimation(this).toBundle()
        )
    }

    /**
     * 新闻列表点击
     */
    override fun mNew(mainNew: NewsList) {
        val intent = Intent(this, WebActivity::class.java)
        val mUrl = "${PreApi.xmlUrl}Information/BlogPostSingal/${mainNew.ID}"
        intent.putExtra("mUrl", mUrl)
        intent.putExtra("mTitle", "资讯详情")
        myStartActivity(intent, this)
    }

    /**
     * 获取新闻列表
     */
    private fun getNewsList() {
        subscription = CompositeSubscription()
        subscription?.add(
            DataManager.getNewsList(0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<NewsDTO<List<NewsList>>>(this) {
                    override fun onNext(t: NewsDTO<List<NewsList>>?) {
                        newsList = t
                    }

                    override fun onCompleted() {
                        newsManager()
                    }
                })
        )
    }

    private fun newsManager() {
        if (newsList == null) {
            MyToast.makeS(this, "请求失败")
            return
        }
        mainNewAdapter.setNewList(newsList!!.Results!!)
    }

    /**
     * 修改后更新当前页
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            100 -> initData()
            200 -> getFindInfo()
        }
    }

    /**
     * 计算头部滑动
     */
    override fun onOffsetChanged(p0: AppBarLayout?, p1: Int) {
        //垂直方向偏移量
        val offset: Int = abs(p1)
        //最大偏移距离
        val scrollRange: Int = p0!!.totalScrollRange
        if (offset == 0) {
            main_toolbar.visibility = View.GONE
        } else {
            main_toolbar.visibility = View.VISIBLE
        }
        if (offset <= scrollRange / 2) { //当滑动没超过一半，展开状态下toolbar显示内容，根据收缩位置，改变透明值
            main_toolbar_close_bg.visibility = View.VISIBLE
            main_toolbar_close.visibility = View.GONE
            //根据偏移百分比 计算透明值
            val scale2 = offset.toFloat() / (scrollRange / 2)
            val alpha2 = (255 * scale2).toInt()
            toolbar_open_view_bg.setBackgroundColor(Color.argb(alpha2, 59, 195, 207))
        } else { //当滑动超过一半，收缩状态下toolbar显示内容，根据收缩位置，改变透明值
            main_toolbar_close.visibility = View.VISIBLE
            main_toolbar_close_bg.visibility = View.GONE
            val scale3 = (scrollRange - offset).toFloat() / (scrollRange / 2)
            val alpha3 = (255 * scale3).toInt()
            toolbar_close_view_bg.setBackgroundColor(Color.argb(alpha3, 59, 195, 207))
        }
        //根据偏移百分比所有信息的透明度值
        val scale = offset.toFloat() / scrollRange
        val alpha = (255 * scale).toInt()
        toolbar_open_bg.setBackgroundColor(Color.argb(alpha, 59, 195, 207))
    }

    override fun onDestroy() {
        super.onDestroy()
        main_app_bar.removeOnOffsetChangedListener(this)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit()
            return false
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exit() {
        if (System.currentTimeMillis() - exitTime > 2000) {
            MyToast.makeS(this, "再按一次退出程序")
            exitTime = System.currentTimeMillis()
        } else {
            MyApplication.myApp?.closeApp()
        }
    }
}
