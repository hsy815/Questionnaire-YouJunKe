package com.biotecan.questionnaire.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.activity.QuestionActivity
import com.biotecan.questionnaire.entity.PreAnswers
import com.biotecan.questionnaire.entity.Presets
import com.biotecan.questionnaire.entity.SubmitItem
import com.biotecan.questionnaire.util.DpUtil
import kotlinx.android.synthetic.main.fragment_radio.*

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.fragment
 * @创始人: hsy
 * @创建时间: 2020/5/8 13:52
 * @类描述: 单选加额外输入的Fragment
 * @修改人: hsy
 * @修改时间: 2020/5/8 13:52
 * @修改描述:
 */
class RadioFragment(private val position: Int) : Fragment() {

    private lateinit var questionActivity: QuestionActivity
    private lateinit var mPresets: Presets //当个问卷题目集合
    private var mPreAnswers: PreAnswers? = null//被选中的选项
    private var mEditText: EditText? = null//附加输入EditText
    private var mId: Int = -1//被选中的id

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
        return inflater.inflate(R.layout.fragment_radio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        radio_title.text = mPresets.Question
        addRadioGroup()
        radio_group.setOnCheckedChangeListener { group, _ ->
            val radioBtn = questionActivity.findViewById<RadioButton>(group.checkedRadioButtonId)
            val rank = radioBtn.id
            val checkedStr = radioBtn.tag.toString()

            if (mId != rank) {//判断上次选中的id是否与当前选中相同  如相同说明再次点击了该选项  不相同进入方法体
                if (null != mEditText) {//在id相同的情况下   判断EditText是否存在若存在删除原有EditText
                    radio_group.removeView(mEditText)
                    mEditText = null
                }
            }

            mPreAnswers = mPresets.PreAnswers[rank]//取出被选中选项
            if (mPreAnswers!!.AllowAdditionalInput && null == mEditText) {//判断被选中选项是否需要附加项 且 mEditText是否为null 不为null则输入框已存在不需要在new
                mEditText = newEditView("", checkedStr)
                radio_group.addView(mEditText, rank + 1)
            }

            mId = rank
        }
    }

    /**
     * 添加View
     */
    private fun addRadioGroup() {
        if (questionActivity.questionSubmitItem.size > position) {
            val submitItem = questionActivity.questionSubmitItem[position]
            addRecoverRadio(submitItem)
        } else {
            addNewRadio()
        }
    }

    /**
     * 恢复（fragment右滑返回）
     * @submitItem 之前已经做好的答案
     */
    private fun addRecoverRadio(submitItem: SubmitItem) {
        for (preAnswers: PreAnswers in mPresets.PreAnswers) {
            if (preAnswers.BN == submitItem.ValueKey) {
                mPreAnswers = preAnswers
                radio_group.addView(
                    newRadioView(
                        preAnswers.DisplayValue,
                        true,
                        preAnswers.Rank,
                        preAnswers.BN
                    )
                )
                if (preAnswers.AllowAdditionalInput) {
                    radio_group.addView(newEditView(submitItem.PlusValue, preAnswers.BN))
                }
            } else {
                radio_group.addView(
                    newRadioView(
                        preAnswers.DisplayValue,
                        false,
                        preAnswers.Rank,
                        preAnswers.BN
                    )
                )
            }
        }
    }

    /**
     * 全新的问卷问题RadioGroup新建
     */
    private fun addNewRadio() {
        for (preAnswers: PreAnswers in mPresets.PreAnswers) {
            radio_group.addView(
                newRadioView(
                    preAnswers.DisplayValue,
                    false,
                    preAnswers.Rank,
                    preAnswers.BN
                )
            )
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
        return if (null == mPreAnswers) {
            SubmitItem(
                mPresets.BN,
                "",
                "",
                getPlusValue()
            )
        } else {
            SubmitItem(
                mPresets.BN,
                mPreAnswers!!.BN,
                mPreAnswers!!.DisplayValue,
                getPlusValue()
            )
        }
    }

    /**
     * 获取额外输入数据
     */
    private fun getPlusValue(): String {
        if (null == mEditText) return ""
        return mEditText!!.text.toString()
    }

    /**
     * 新建radioButton
     *  @str 选项文字内容
     *  @checked 是否选中选项
     *  @rank 选项id
     *  @tag 选项tag
     */
    private fun newRadioView(str: String, checked: Boolean, rank: Int, tag: String): RadioButton {
        val radioButton = RadioButton(questionActivity)
        radioButton.id = rank
        radioButton.tag = tag
        radioButton.text = str
        radioButton.setTextColor(ContextCompat.getColor(questionActivity, R.color.login_hello_t))
        radioButton.isChecked = checked
        radioButton.textSize = 16f
        radioButton.setButtonDrawable(R.drawable.radio_question_bg)
        val layout = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            RadioGroup.LayoutParams.MATCH_PARENT
        )
        layout.topMargin = DpUtil.dip2px(questionActivity, 25f)
        radioButton.layoutParams = layout
        radioButton.setPadding(DpUtil.dip2px(questionActivity, 9f), 0, 0, 0)
        return radioButton
    }

    /**
     * 新建EditText
     * @str 输入的内容
     * @checkedTag 标记
     */
    private fun newEditView(str: String?, checkedTag: String): EditText {
        val editText = EditText(questionActivity)
        val padding = DpUtil.dip2px(questionActivity, 8f)
        editText.setPadding(padding, padding, padding, padding)
        val layout = RadioGroup.LayoutParams(
            RadioGroup.LayoutParams.MATCH_PARENT,
            DpUtil.dip2px(questionActivity, 60f)
        )
        layout.topMargin = DpUtil.dip2px(questionActivity, 10f)
        editText.setText(str)
        editText.tag = checkedTag
        editText.layoutParams = layout
        editText.gravity = Gravity.START
        editText.hint = "请输入"
        editText.textSize = 16f
        editText.background =
            ContextCompat.getDrawable(questionActivity, R.drawable.question_edit_bg)
        return editText
    }
}