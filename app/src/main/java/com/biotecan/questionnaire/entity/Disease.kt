package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/4/20 9:25
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/20 9:25
 * @修改描述:
 */
class Disease(
    var Name: String,
    var EName: String,
    var Abbr: String,
    var Summary: String,
    var BN: String
) {
    var isSelect: Boolean = false
}