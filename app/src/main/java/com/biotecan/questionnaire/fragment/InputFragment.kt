package com.biotecan.questionnaire.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.activity.QuestionActivity
import com.biotecan.questionnaire.entity.Presets
import com.biotecan.questionnaire.entity.SubmitItem
import kotlinx.android.synthetic.main.fragment_input.*

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.fragment
 * @创始人: hsy
 * @创建时间: 2020/5/9 15:17
 * @类描述: 仅输入类问卷问题Fragment
 * @修改人: hsy
 * @修改时间: 2020/5/9 15:17
 * @修改描述:
 */
class InputFragment(private val position: Int) : Fragment() {

    private lateinit var questionActivity: QuestionActivity
    private lateinit var mPresets: Presets //当个问卷题目集合

    override fun onAttach(context: Context) {
        super.onAttach(context)
        questionActivity = activity as QuestionActivity
        mPresets = questionActivity.questionPresets!![position]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_input, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        input_title.text = mPresets.Question
        setEditText()
    }

    /**
     * 根据情况给EditText赋值
     */
    private fun setEditText() {
        if (questionActivity.questionSubmitItem.size > position) {
            val submitItem = questionActivity.questionSubmitItem[position]
            input_edit.setText(submitItem.Value)
        }
    }

    override fun onPause() {
        saveSubmitData()
        super.onPause()
    }

    /**
     * 保存要提交的问卷题目的答案
     */
    private fun saveSubmitData() {
        if (questionActivity.questionSubmitItem.size > position) {
            questionActivity.questionSubmitItem[position] = getSubmitItem()
        } else {
            questionActivity.questionSubmitItem.add(getSubmitItem())
        }
    }

    /**
     * 整理要保存的本页面数据
     */
    private fun getSubmitItem(): SubmitItem {
        return SubmitItem(
            mPresets.BN,
            "",
            input_edit.text.toString(),
            ""
        )
    }
}