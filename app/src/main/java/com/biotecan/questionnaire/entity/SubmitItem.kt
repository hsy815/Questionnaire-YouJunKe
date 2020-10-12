package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/5/9 14:04
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/9 14:04
 * @修改描述:
 */
class SubmitItem(
    /**
     * 问题(键值)
     */
    var ItemKey: String?,
    /**
     * 答案(键值)
     */
    var ValueKey: String?,
    /**
     * 答案
     */
    var Value: String?,
    /**
     * 附加答案
     */
    var PlusValue: String?
) {
}