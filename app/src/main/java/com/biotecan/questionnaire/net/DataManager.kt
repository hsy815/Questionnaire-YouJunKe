package com.biotecan.questionnaire.net

import com.biotecan.questionnaire.entity.*
import com.biotecan.questionnaire.pre.PreApi
import okhttp3.RequestBody
import rx.Observable

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.net
 * @创始人: hsy
 * @创建时间: 2020/4/8 16:03
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 16:03
 * @修改描述:
 */
object DataManager {

    fun getWXAccess(appid: String, secret: String, code: String?): Observable<WXAccess> {
        return RqRetrofit.rRetrofit(PreApi.wxAccessUrl)
            .getWXAccess(appid, secret, code, "authorization_code")
    }

    fun getWXUserInfo(access_token: String, openid: String): Observable<WXUserInfo> {
        return RqRetrofit.rRetrofit(PreApi.wxAccessUrl)
            .getWXUserInfo(access_token, openid)
    }

    fun getUpDataInfo(userInfo: UserInfo): Observable<OptResultDTO<UserInfo>> {
        return RqRetrofit.rRetrofit().getUpDataInfo(userInfo)
    }

    fun getSecretToken(requestBody: RequestBody): Observable<SecretToken> {
        return RqRetrofit.rRetrofit().getSecretToken(requestBody)
    }

    fun getSendCode(phone: String): Observable<OptResultDTO<String>> {
        return RqRetrofit.rRetrofit().getSendCode(phone)
    }

    fun getSignIn(phoneOrSns: String): Observable<OptResultDTO<UserInfo>> {
        return RqRetrofit.rRetrofit().getSignIn(phoneOrSns)
    }

    fun getFindInfo(bn: String): Observable<OptResultDTO<UserInfo>> {
        return RqRetrofit.rRetrofit().getFindInfo(bn)
    }

    fun getDiseaseList(): Observable<OptResultDTO<List<Disease>>> {
        return RqRetrofit.rRetrofit().getDiseaseList()
    }

    fun getDataRecruitFormApi(
        audienceBn: String,
        typeBn: String
    ): Observable<OptResultDTO<QuestionResult>> {
        return RqRetrofit.rRetrofit().getDataRecruitFormApi(audienceBn, typeBn)
    }

    fun getDataSubmit(submitMap: HashMap<String, String?>): Observable<OptResultDTO<String>> {
        return RqRetrofit.rRetrofit().getDataSubmit(submitMap)
    }

    fun getRegister(map: Map<String, String>): Observable<OptResultDTO<UserInfo>> {
        return RqRetrofit.rRetrofit().getRegister(map)
    }

    fun getFindAudienceData(bn: String): Observable<OptResultDTO<List<FindAudience>>> {
        return RqRetrofit.rRetrofit().getFindAudienceData(bn)
    }

    fun getExistByAudience(audienceBn: String, typeBn: String): Observable<OptResultDTO<Boolean>> {
        return RqRetrofit.rRetrofit().getExistByAudience(audienceBn, typeBn)
    }

    fun changeBindPhone(changePhone: ChangePhone): Observable<OptResultDTO<UserInfo>> {
        return RqRetrofit.rRetrofit().changeBindPhone(changePhone)
    }

    fun getNewsList(num: Int): Observable<NewsDTO<List<NewsList>>> {
        return RqRetrofit.rRetrofit().getNewsList(num)
    }
}