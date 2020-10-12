package com.biotecan.questionnaire.entity

import java.math.BigDecimal

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.entity
 * @创始人: hsy
 * @创建时间: 2020/4/16 9:47
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/16 9:47
 * @修改描述:
 */
class PreAnswers(
    /**
     * 序号
     */
    var Rank: Int,
    /**
     * 字符预设值
     */
    var StringValue: String,
    /**
     * 整数预设值
     */
    var IntValue: Int,
    /**
     * 浮点预设值
     */
    var DecimalValue: BigDecimal,
    /**
     * 显示预设值
     */
    var DisplayValue: String,
    /**
     * 分值
     */
    var Score: Int,
    /**
     * 多选排他选项
     */
    var ExclusiveSelectOption: Boolean,
    /**
     * 附加输入
     */
    var AllowAdditionalInput: Boolean,
    /**
     * 附加输入格式
     */
    var AdditionalInputFormat: String,
    var BN: String
) {
}