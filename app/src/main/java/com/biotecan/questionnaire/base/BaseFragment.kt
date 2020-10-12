package com.biotecan.questionnaire.base

import androidx.fragment.app.Fragment

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.base
 * @创始人: hsy
 * @创建时间: 2020/5/12 11:54
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/12 11:54
 * @修改描述:
 */
abstract class BaseFragment : Fragment() {

    abstract fun saveItemFragmentData()
}