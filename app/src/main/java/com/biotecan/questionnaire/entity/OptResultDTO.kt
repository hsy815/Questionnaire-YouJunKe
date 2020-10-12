package com.biotecan.questionnaire.entity

import java.io.Serializable

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/4/8 17:14
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 17:14
 * @修改描述:
 */
class OptResultDTO<T> : Serializable {
    var OperatingStatus: Boolean = false
    var Message: String? = null
    var DataResult: T? = null
}