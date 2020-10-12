package com.biotecan.questionnaire.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.activity.QuestionActivity
import com.biotecan.questionnaire.entity.PreAnswers
import com.biotecan.questionnaire.entity.Presets
import com.biotecan.questionnaire.entity.SubmitItem
import com.biotecan.questionnaire.util.DpUtil
import kotlinx.android.synthetic.main.fragment_checkbox.*
import java.util.*
import kotlin.collections.HashMap

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.fragment
 * @创始人: hsy
 * @创建时间: 2020/5/9 15:34
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/9 15:34
 * @修改描述:
 */
class CheckboxFragment(private val position: Int) : Fragment() {

    private lateinit var questionActivity: QuestionActivity
    private lateinit var mPresets: Presets //当个问卷题目集合
    private val selectMap: HashMap<String, EditText> = HashMap()//存储额外输入值的EditText
    private var excludeCheckBox: CheckBox? = null//排它CheckBox

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
        return inflater.inflate(R.layout.fragment_checkbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        checkbox_title.text = mPresets.Question
        addCheckLin()
    }

    /**
     * 处理是否为新建view还是返回显示上一页内容
     */
    private fun addCheckLin() {
        if (questionActivity.questionSubmitItem.size > position) {
            //因为在多选里面questionActivity.questionSubmitItem[position] 中的SubmitItem对象只是占位作用
            //所以此处取出的SubmitItem的key（问卷问题的BN）和进入本页面的mPresets对象BN是同一个
            //故在下面方法中直接使用mPresets.BN
            isRecoverCheck()
        } else {
            addNewCheck()
        }
    }

    /**
     * 处理返回上一页内容
     * 上一页内容曾经是否有做操作
     */
    private fun isRecoverCheck() {
        val submitList = questionActivity.questionMap[mPresets.BN]
        if (null != submitList && submitList.size > 0) {
            addRecoverCheck(submitList)
        } else {
            addNewCheck()
        }
    }

    /**
     * 恢复已经操作过的问卷问题
     */
    private fun addRecoverCheck(submitList: ArrayList<SubmitItem>) {

        w@ for (preAnswers: PreAnswers in mPresets.PreAnswers) {
            for (submit: SubmitItem in submitList) {
                if (preAnswers.BN == submit.ValueKey) {
                    checkbox_lin.addView(
                        newCheckbox(
                            preAnswers.DisplayValue,
                            true,
                            preAnswers.Rank,
                            preAnswers.BN,
                            preAnswers.AllowAdditionalInput,
                            preAnswers.ExclusiveSelectOption
                        )
                    )
                    if (preAnswers.AllowAdditionalInput) {
                        checkbox_lin.addView(newEditView(submit.PlusValue, preAnswers.BN))
                    }
                    continue@w
                }
            }

            checkbox_lin.addView(
                newCheckbox(
                    preAnswers.DisplayValue,
                    false,
                    preAnswers.Rank,
                    preAnswers.BN,
                    preAnswers.AllowAdditionalInput,
                    preAnswers.ExclusiveSelectOption
                )
            )
        }
    }

    /**
     * 添加新的CheckBox Or EditText
     */
    private fun addNewCheck() {
        for (preAnswers: PreAnswers in mPresets.PreAnswers) {
            checkbox_lin.addView(
                newCheckbox(
                    preAnswers.DisplayValue,
                    false,
                    preAnswers.Rank,
                    preAnswers.BN,
                    preAnswers.AllowAdditionalInput,
                    preAnswers.ExclusiveSelectOption
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

        questionActivity.questionMap[mPresets.BN] = getSubmitItemList()
    }

    /**
     * 遍历存储选中数据
     */
    private fun getSubmitItemList(): ArrayList<SubmitItem>? {
        val cheLinCount = checkbox_lin.childCount
        if (cheLinCount <= 0) return null

        val submitList = ArrayList<SubmitItem>()
        for (i: Int in 0 until (cheLinCount - 1)) {//遍历取出LineLayout中的子View
            val view = checkbox_lin.getChildAt(i)
            if (view is CheckBox) {//view是否是CheckBox
                if (view.isChecked) {//此CheckBox被选中
                    view.tag
                    view.text
                    /**
                     * 取出对应id的选项对象
                     * 当然也可以不用取出该对象
                     * 直接使用:
                     * view.tag(view.tag=preAnswers.BN)，
                     * view.text(view.text=preAnswers.DisplayValue)
                     */
                    val preAnswers = mPresets.PreAnswers[(view.id - 9999)]
                    var plusValue = ""
                    if ((i + 1) < cheLinCount) {//避免下标越界
                        plusValue = getPlusValue(checkbox_lin.getChildAt(i + 1))//取出额外输入值
                    }
                    val sub = SubmitItem(
                        mPresets.BN,
                        preAnswers.BN,
                        preAnswers.DisplayValue,
                        plusValue
                    )
                    submitList.add(sub)
                }
            }
        }
        return submitList
    }

    /**
     * （注意： 多选页面此方法只提供一个带有属于该页面Key的空对象 也就是占位对象
     *         多选题目的问卷答案保存在 {@link questionActivity.questionMap}）
     * 整理要保存的本页面数据
     */
    private fun getSubmitItem(): SubmitItem {
        return SubmitItem(
            mPresets.BN,
            "",
            "",
            ""
        )
    }

    /**
     * 获取额外输入数据
     */
    private fun getPlusValue(view: View): String {
        if (view !is EditText) return ""
        return view.text.toString()
    }

    /**
     * 新建radioButton
     *  @str 选项文字内容
     *  @checked 是否选中选项
     *  @rank 选项id
     *  @tag 选项tag
     */
    private fun newCheckbox(
        str: String,
        checked: Boolean,
        rank: Int,
        tag: String,
        isInput: Boolean,
        isExclude: Boolean
    ): CheckBox {
        val checkBox = CheckBox(questionActivity)
        checkBox.id = (rank + 9999)//避免id重复
        checkBox.tag = tag
        checkBox.text = str
        checkBox.setTextColor(ContextCompat.getColor(questionActivity, R.color.login_hello_t))
        checkBox.isChecked = checked
        checkBox.textSize = 16f
        checkBox.setButtonDrawable(R.drawable.check_question_bg)
        val layout = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        layout.topMargin = DpUtil.dip2px(questionActivity, 25f)
        checkBox.layoutParams = layout
        checkBox.setPadding(DpUtil.dip2px(questionActivity, 9f), 0, 0, 0)
        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (isExclude) {
                    excludeOtherCheck()
                } else {
                    excludeCheckBox?.isChecked = false
                }
            }
            if (!isInput) return@setOnCheckedChangeListener
            if (isChecked) {
                val editText = newEditView("", tag)
                selectMap[tag] = editText
                checkbox_lin.addView(editText, (rank + selectMap.size))
            } else {
                val editText = selectMap[tag]
                if (null != editText) {
                    checkbox_lin.removeView(editText)
                    selectMap.remove(tag)
                }
            }
        }
        if (isExclude)
            excludeCheckBox = checkBox
        return checkBox
    }

    private fun excludeOtherCheck() {
        val cheLinCount = checkbox_lin.childCount
        if (cheLinCount <= 0) return

        for (i: Int in 0 until (cheLinCount - 1)) {//遍历取出LineLayout中的子View
            val view = checkbox_lin.getChildAt(i)
            if (view is CheckBox) {//view是否是CheckBox
                if (view != excludeCheckBox)
                    view.isChecked = false
                if ((i + 1) < cheLinCount) {//避免下标越界
                    if (checkbox_lin.getChildAt(i + 1) is EditText) {
                        checkbox_lin.removeView(view)
                    }
                }
            }
        }
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