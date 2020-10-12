package com.biotecan.questionnaire.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

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
class SeeFragmentAdapter(fm: FragmentManager, private val fragment: List<Fragment>) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

    override fun getCount(): Int {
        return fragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "未填"
            else -> "已填"
        }
    }
}