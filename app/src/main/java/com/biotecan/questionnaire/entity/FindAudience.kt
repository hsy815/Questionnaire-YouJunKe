package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/5/21 10:08
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/21 10:08
 * @修改描述:
 */
class FindAudience(
    var BN: String,
    var Name: String,
    var RTime: String,
    var AudienceKey: String,
    var SchemeInstanceKey: String,
    var StepRank: Int,
    var ContentRank: Int,
    var Source: Int,
    var Assessment: Int,
    var PhysicianKey: String,
    var NeedAssessment: Boolean,
    var Description: String,
    var Items: List<FindAudienceItem>
) {
}