package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/4/8 16:19
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 16:19
 * @修改描述:
 */
class WXUserInfo(
    var openid: String,
    var nickname: String,
    var headimgurl: String,
    var unionid: String,
    var sex: Int
) {
}