package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/4/16 9:51
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/16 9:51
 * @修改描述:
 */
class Presets(
    var Rank: Int,
    var GroupRank: Int,
    var GroupTitle: String,
    var Question: String,
    var Type: Int,
    var ValueType: Int,
    var ValueRecruitFormat: String,
    var Description: String,
    var PreAnswers: ArrayList<PreAnswers>,
    var BN: String
) {
}