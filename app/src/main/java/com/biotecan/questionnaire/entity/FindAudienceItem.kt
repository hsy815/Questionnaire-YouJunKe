package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/5/21 10:12
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/21 10:12
 * @修改描述:
 */
class FindAudienceItem(
    var BN: String,
    var GroupName: String,
    var GroupRank: Int,
    var Question: String,
    var Rank: Int,
    var Type: Int,
    var DisplayValue: String,
    var Score: Int,
    var PlusValue: String,
    var ValueRecruitFormat: String,
    var Description: String,
    var AdditionalInputFormat: String
) {
}