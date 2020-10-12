package com.biotecan.questionnaire.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.transition.Explode
import androidx.recyclerview.widget.LinearLayoutManager
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.adapter.MainNewAdapter
import com.biotecan.questionnaire.base.BaseActivity
import com.biotecan.questionnaire.entity.NewsDTO
import com.biotecan.questionnaire.entity.NewsList
import com.biotecan.questionnaire.net.DataManager
import com.biotecan.questionnaire.net.MySubscriber
import com.biotecan.questionnaire.pre.PreApi
import com.biotecan.questionnaire.util.MyToast
import com.biotecan.questionnaire.util.StatusBarUtil
import com.hsy.refershloading.view.OrdinaryPDLView
import kotlinx.android.synthetic.main.activity_news_list.*
import kotlinx.android.synthetic.main.best_top_layout.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

class NewsListActivity : BaseActivity(), OrdinaryPDLView.onRefreshListener {

    private var newsList: NewsDTO<List<NewsList>>? = null
    private lateinit var mainNewAdapter: MainNewAdapter
    private var mCount: Int = 0
    private var mDataCount: Int = 0

    override fun init(savedInstanceState: Bundle?) {
        window.statusBarColor = Color.WHITE
        window.enterTransition = Explode()
    }

    override fun setLayoutResourceID(): Int {
        return R.layout.activity_news_list
    }

    override fun initUI() {
        StatusBarUtil.setBarUI(this, true)//因为状态栏设置白色  需要吧状态栏字体颜色反转
        best_top_title.text = "新闻资讯"
        best_top_back.setOnClickListener { onBackPressed() }
        news_pdl.moveDistanceTop = OrdinaryPDLView.MoveDistanceAll
        news_pdl.moveDistanceBtn = OrdinaryPDLView.MoveDistanceAll
        news_pdl.setOnRefreshListener(this)
        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        news_cyc.layoutManager = layoutManager
        mainNewAdapter = MainNewAdapter(this, 9999)
        news_cyc.adapter = mainNewAdapter
        mainNewAdapter.setOnItemListener(object : MainNewAdapter.OnItemListener {
            override fun mNew(mainNew: NewsList) {
                val intent = Intent(this@NewsListActivity, WebActivity::class.java)
                val mUrl = "${PreApi.xmlUrl}Information/BlogPostSingal/${mainNew.ID}"
                intent.putExtra("mUrl", mUrl)
                intent.putExtra("mTitle", "资讯详情")
                myStartActivity(intent, this@NewsListActivity)
            }
        })
        getData(mCount)
    }

    private fun getData(num: Int) {
        loadingDialog.show()
        subscription = CompositeSubscription()
        subscription?.add(
            DataManager.getNewsList(num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MySubscriber<NewsDTO<List<NewsList>>>(this) {
                    override fun onStart() {
                        super.onStart()
                        news_pdl.stopLoadMore()
                        news_pdl.stopRefresh()
                    }

                    override fun onNext(t: NewsDTO<List<NewsList>>?) {
                        newsList = t
                    }

                    override fun onError(e: Throwable?) {
                        super.onError(e)
                        loadingDialog.dismiss()
                        news_pdl.stopLoadMore()
                        news_pdl.stopRefresh()
                    }

                    override fun onCompleted() {
                        newsManager()
                    }
                })
        )
    }

    private fun newsManager() {
        loadingDialog.dismiss()
        news_pdl.stopLoadMore()
        news_pdl.stopRefresh()
        if (newsList == null) {
            MyToast.makeS(this, "请求失败")
            return
        }
        mainNewAdapter.addNewList(newsList!!.Results!!)
        mCount++
    }

    override fun onLoadMore() {
        mDataCount = newsList!!.Count
        if (mDataCount > 10) {
            var pageCount = mDataCount / 10
            val num = mDataCount % 10
            if (num > 0) {
                pageCount += 1
            }
            if (mCount <= pageCount) {
                getData(mCount)
            } else {
                news_pdl.stopLoadMore()
                news_pdl.stopRefresh()
            }
        } else {
            news_pdl.stopLoadMore()
            news_pdl.stopRefresh()
        }
    }

    override fun onRefresh() {
        mainNewAdapter.newsClear()
        mCount = 0
        getData(mCount)
    }
}