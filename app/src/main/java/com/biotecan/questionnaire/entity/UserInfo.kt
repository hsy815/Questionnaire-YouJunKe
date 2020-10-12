package com.biotecan.questionnaire.entity

import java.math.BigDecimal

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.activity
 * @创始人: hsy
 * @创建时间: 2020/4/14 11:17
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/14 11:17
 * @修改描述:
 */
class UserInfo(
    /**
     * 受众编号
     */
    var Code: String,//受众编号
    /**
     * 受众姓名
     */
    var Name: String,//受众姓名
    /**
     * 移动电话
     */
    var MobilePhone: String,//移动电话
    /**
     * 受众类别 未划分 = 0,  病患 = 1,  供体人 = 2
     */
    var Kind: Int,//受众类别 未划分 = 0,  病患 = 1,  供体人 = 2
    /**
     * 性别  未知 = 0,  男性 = 1,  女性 = 2
     */
    var Gender: Int,//性别  未知 = 0,  男性 = 1,  女性 = 2
    /**
     * 出生日期
     */
    var DateOfBirth: String,//出生日期
    /**
     * 当前身高(cm)
     */
    var Stature: BigDecimal,//当前身高(cm)
    /**
     * 当前体重(kg)
     */
    var BodyWeight: BigDecimal,//当前体重(kg)
    /**
     * 联络地址
     */
    var ContactAddress: String,//联络地址
    /**
     * 问卷类型
     */
    var SchemeInstances: List<SchemeInstances>,//问卷类型
    /**
     * 电子邮箱
     */
    var Email: String,//电子邮箱
    /**
     * 民族
     */
    var Folk: String,//民族
    /**
     * 籍贯
     */
    var NativePlace: String,//籍贯
    /**
     * 教育程度  选项：初中及以下;高中(含中专中职);大学/研究生及以上
     */
    var Education: String,//教育程度  选项：初中及以下;高中(含中专中职);大学/研究生及以上
    /**
     * 职业  选项：在职;自由职业;学生(含休学);无业;退休
     */
    var JobStatus: String,//职业  选项：在职;自由职业;学生(含休学);无业;退休
    /**
     * 家庭年收入  选项：1万以下;1-5万;5-20万;20万以上
     */
    var AnnualHouseholdIncome: String,//家庭年收入  选项：1万以下;1-5万;5-20万;20万以上
    /**
     * SNS账号
     */
    var SNS: String,//SNS账号
    /**
     * 备注  最大长度300
     */
    var Remark: String,//备注  最大长度300
    /**
     * 是否收录
     */
    var Recorded: Boolean,//是否收录
    /**
     * Base64 图像头像
     */
    var HeadPortrait: String,//Base64 图像头像
    var BN: String
) {
}