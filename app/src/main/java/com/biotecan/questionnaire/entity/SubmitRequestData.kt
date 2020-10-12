package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/5/12 9:33
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/12 9:33
 * @修改描述:
 */
class SubmitRequestData(
    /**
     * 方案内容表单(键值)
     */
    var ContentKey: String,
    /**
     * 受众(键值)
     */
    var AudienceKey: String,
    /**
     * 问题答案列表(s)
     */
    var Items: ArrayList<SubmitItem>
) {
}