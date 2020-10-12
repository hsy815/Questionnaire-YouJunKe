package com.biotecan.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.biotecan.questionnaire.entity.Presets
import com.biotecan.questionnaire.fragment.CheckboxFragment
import com.biotecan.questionnaire.fragment.InputFragment
import com.biotecan.questionnaire.fragment.RadioFragment

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.adapter
 * @创始人: hsy
 * @创建时间: 2020/5/8 14:32
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/8 14:32
 * @修改描述:
 */
class QuestionFragmentAdapter(fm: FragmentManager, private val presetsList: List<Presets>) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (presetsList[position].Type) {
            0 -> {//单选及附加
                RadioFragment(position)
            }
            3 -> {//输入
                InputFragment(position)
            }
            else -> {//1、多选 2、多选及附加填入
                CheckboxFragment(position)
            }
        }
    }

    override fun getCount(): Int {
        return presetsList.size
    }
}