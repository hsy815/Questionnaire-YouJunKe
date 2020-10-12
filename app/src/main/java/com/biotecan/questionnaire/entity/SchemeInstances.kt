package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/5/8 16:19
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/8 16:19
 * @修改描述:
 */
class SchemeInstances(
    var Code: String,
    var AudienceKey: String,
    var AudienceCode: String,
    var AudienceName: String,
    var CTime: String,
    var SchemeKey: String,
    var TypeKey: String,
    var TypeName: String,
    var Step: Int,
    var Recorded: Boolean,
    var BN: String
) {

}