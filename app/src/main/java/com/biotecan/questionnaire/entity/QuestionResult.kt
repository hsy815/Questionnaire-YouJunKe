package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/5/8 13:31
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/8 13:31
 * @修改描述:
 */
class QuestionResult(
    var BN: String,
    var Name: String,
    var TypeKey: String,
    var Rank: Int,
    var Source: Int,
    var Description: String,
    var Presets: List<Presets>
) {

}