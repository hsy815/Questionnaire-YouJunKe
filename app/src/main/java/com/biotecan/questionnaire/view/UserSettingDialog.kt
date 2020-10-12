package com.biotecan.questionnaire.view

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.text.TextUtils
import com.biotecan.questionnaire.pre.PreData
import com.biotecan.questionnaire.util.DateAndString
import java.util.*

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.view
 * @创始人: hsy
 * @创建时间: 2020/4/29 9:30
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/29 9:30
 * @修改描述:
 */
class UserSettingDialog(private val mContext: Context) {

    private var mOnItemClick: OnItemClick? = null

    fun clickOption(option: String, value: String?, onItemClick: OnItemClick) {
        mOnItemClick = onItemClick
        var options: Array<String>? = null
        val title = "请选择$option"
        when (option) {
            "性别" -> {
                options = PreData.getGender()
            }
            "教育程度" -> {
                options = PreData.getEducation()
            }
            "职业" -> {
                options = PreData.getJobStatus()
            }
            "家庭年收入" -> {
                options = PreData.getAnnualHouseholdIncome()
            }
            "出生日期" -> {
                getDateOfBirth(value)
                return
            }
        }
        singleDialog(title, options!!.indexOf(value), options)
    }

    private fun getDateOfBirth(value: String?) {
        var mYear = ""
        var mMonth = ""
        var mDay = ""
        if (TextUtils.isEmpty(value)) {
            val calendar = Calendar.getInstance()
            mYear = calendar.get(Calendar.YEAR).toString()
            mMonth = (calendar.get(Calendar.MONTH) + 1).toString()
            mDay = calendar.get(Calendar.DAY_OF_MONTH).toString()
        } else {
            val mDate = value!!.split("-")
            mYear = mDate[0]
            mMonth = mDate[1]
            mDay = mDate[2]
        }

        val dateP = DatePickerDialog(
            mContext,
            DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                mOnItemClick!!.itemValue(DateAndString.getDate2("$year-${month + 1}-$dayOfMonth"))
            }, mYear.toInt(), mMonth.toInt() - 1, mDay.toInt()
        )
        dateP.setTitle("请选择出生日期")
        dateP.show()
    }


    private fun singleDialog(title: String, position: Int, options: Array<String>) {
        AlertDialog.Builder(mContext)
            .setTitle(title)
            .setSingleChoiceItems(
                options, position
            ) { _, which ->
                mOnItemClick!!.itemValue(options[which])
            }
            .setNegativeButton("取消") { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton("确定") { dialog, _ ->
                dialog.dismiss()
            }.create().show()

    }

    interface OnItemClick {
        fun itemValue(value: String)
    }
}