package com.biotecan.questionnaire.net

import com.biotecan.questionnaire.entity.*
import com.biotecan.questionnaire.pre.PreApi
import okhttp3.RequestBody
import retrofit2.http.*
import rx.Observable

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.net
 * @创始人: hsy
 * @创建时间: 2020/4/8 13:46
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 13:46
 * @修改描述:
 */
interface ApiService {

    @JvmSuppressWildcards//kotlin不识别RequestBody
    @POST(PreApi.secretToken)
    fun getSecretToken(@Body requestBody: RequestBody): Observable<SecretToken>

    /**
     * 获取微信登录token
     */
    @GET(PreApi.wxToken)
    fun getWXAccess(
        @Query("appid") appid: CharSequence,
        @Query("secret") secret: CharSequence,
        @Query("code") code: CharSequence?,
        @Query("grant_type") grant_type: CharSequence
    ): Observable<WXAccess>

    /**
     * 获取微信用户信息
     */
    @GET(PreApi.wxUserInfo)
    fun getWXUserInfo(
        @Query("access_token") access_token: CharSequence,
        @Query("openid") openid: CharSequence
    ): Observable<WXUserInfo>

    /**
     * 修改用户信息
     */
    @PATCH(PreApi.upDataInfo)
    fun getUpDataInfo(@Body userInfo: UserInfo): Observable<OptResultDTO<UserInfo>>

    /**
     * 发送验证码
     */
    @GET("${PreApi.sendCode}/{path1}")
    fun getSendCode(
        @Path("path1") phone: CharSequence
    ): Observable<OptResultDTO<String>>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST(PreApi.register)
    fun getRegister(
        @FieldMap map: Map<String, String>
    ): Observable<OptResultDTO<UserInfo>>

    /**
     * 登录
     */
    @GET("${PreApi.signIn}/{path1}")
    fun getSignIn(
        @Path("path1") phoneOrSns: CharSequence
    ): Observable<OptResultDTO<UserInfo>>

    /**
     * 获取用户信息
     */
    @GET("${PreApi.findInfo}/{path1}")
    fun getFindInfo(
        @Path("path1") phoneOrSns: CharSequence
    ): Observable<OptResultDTO<UserInfo>>

    /**
     * 获取患者疾病列表
     */
    @GET(PreApi.diseaseApiList)
    fun getDiseaseList(): Observable<OptResultDTO<List<Disease>>>

    /**
     * 获取采集数据表单
     */
    @GET("${PreApi.dataRecruitFormApi}/{audienceBn}/{typeBn}")
    fun getDataRecruitFormApi(
        @Path("audienceBn") audienceBn: CharSequence,
        @Path("typeBn") typeBn: CharSequence
    ): Observable<OptResultDTO<QuestionResult>>

    /**
     * 提交采集数据表单
     */
    @FormUrlEncoded
    @POST(PreApi.dataSubmit)
    fun getDataSubmit(
        @FieldMap submitMap: HashMap<String, String?>
    ): Observable<OptResultDTO<String>>

    /**
     * 查询受众数据采集方案实例相应的受众数据采集表单
     */
    @GET("${PreApi.findAudienceData}/{bn}")
    fun getFindAudienceData(
        @Path("bn") bn: CharSequence
    ): Observable<OptResultDTO<List<FindAudience>>>

    /**
     * 是否存在数据采集表单
     */
    @GET("${PreApi.existByAudience}/{audienceBn}/{typeBn}")
    fun getExistByAudience(
        @Path("audienceBn") audienceBn: CharSequence,
        @Path("typeBn") typeBn: CharSequence
    ): Observable<OptResultDTO<Boolean>>

    /**
     * 变更手机号
     */
    @PATCH(PreApi.changeBindPhone)
    fun changeBindPhone(
        @Body changePhone: ChangePhone
    ): Observable<OptResultDTO<UserInfo>>

    /**
     * 获取新闻列表
     */
    @GET("${PreApi.newsList}/{path}")
    fun getNewsList(
        @Path("path") num: Int
    ): Observable<NewsDTO<List<NewsList>>>

}