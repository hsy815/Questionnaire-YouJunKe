package com.biotecan.questionnaire.pre

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.pre
 * @创始人: hsy
 * @创建时间: 2020/4/8 13:47
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 13:47
 * @修改描述:
 */
object PreApi {

    const val url = "https://crfapi.biotecan.com/"
//    const val url = "http://10.5.30.154:2467/"

    const val xmlUrl = "https://intm.biotecan.com/"

    const val wxAccessUrl = "https://api.weixin.qq.com/"//获取微信token链接

    const val wxToken = "sns/oauth2/access_token"//获取微信token api
    const val wxUserInfo = "sns/userinfo"//获取微信用户信息

    const val secretToken = "SecretToken"//获取APP token
    const val signIn = "api/AudienceApi/SignIn"//登录
    const val sendCode = "api/SmsApi/SendVerifyCode"//发送验证码
    const val register = "api/AudienceApi/Register"//注册
    const val findInfo = "api/AudienceApi/Find"//获取用户信息
    const val upDataInfo = "api/AudienceApi/PatchUpdate"//修改用户信息
    const val dataRecruitFormApi = "api/DataRecruitSchemeFormApi/SingleByAudience"//获取采集数据表单
    const val dataSubmit = "api/AudienceDataRecruitFormApi/Submit"//提交采集数据表单
    const val diseaseApiList = "api/DataRecruitTypeApi/List"//获取患者疾病列表
    const val findAudienceData =
        "api/AudienceDataRecruitFormApi/FindAudienceDataRecruitForm"//查询受众数据采集方案实例相应的受众数据采集表单
    const val existByAudience = "api/DataRecruitSchemeFormApi/ExistByAudience"//是否存在数据采集表单(受众)
    const val changeBindPhone = "api/AudienceApi/ChangeBindPhone"//变更手机号
    const val newsList = "${xmlUrl}api/information/"//获取新闻列表
    const val singleHtml = "${url}api/AudienceDataRecruitFormApi/SingleHtml/"//获取单一受众问卷答完后数据
}