package com.biotecan.questionnaire.entity

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/4/27 9:28
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/27 9:28
 * @修改描述:
 */
class UserSetting(
    /**
     * 此项标题
     */
    var title: String,
    /**
     * 类型
     * 0.仅输入  1.图片  2.仅选项  3.不可修改信息
     */
    var type: Int,
    /**
     * 字段名
     */
    var fieldName: String,
    /**
     * 输入的内容
     */
    var value: String?,
    /**
     * 临时输入的内容，用于记录是否有做修改
     */
    var temporaryValue: String,
    /**
     * 输入类型
     * 0.普通文字无限制  1.文字限制10个字（暂定） 2.数字（长度5） 3.手机号（长度11）
     */
    var editType: Int
) {
}