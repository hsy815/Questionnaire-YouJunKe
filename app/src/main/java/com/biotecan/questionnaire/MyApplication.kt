package com.biotecan.questionnaire

import android.app.Activity
import android.app.Application
import android.util.Base64
import com.biotecan.questionnaire.activity.BindNewPhoneActivity
import com.biotecan.questionnaire.activity.OpenIdentityActivity
import com.biotecan.questionnaire.activity.ReplacePhoneCodeActivity
import com.biotecan.questionnaire.entity.SecretToken
import com.biotecan.questionnaire.entity.UserInfo
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.apache.http.util.EncodingUtils

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire
 * @创始人: hsy
 * @创建时间: 2020/4/3 15:14
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/3 15:14
 * @修改描述:
 */
class MyApplication : Application() {

    /**
     * 请求APP Token 后台约束值
     */
    val baseStr = Base64.encodeToString(
        EncodingUtils.getAsciiBytes("8PqaWilv:oPMOQF6xk1YpluczOZGo"),
        Base64.DEFAULT
    )

    /**
     * 请求回来的APP Token
     */
    var secretToken: SecretToken? = null

    /**
     * 用户信息
     */
    var appUserInfo: UserInfo? = null

    /**
     * 更改手机号 旧的手机号
     */
    var oldPhone: String = ""

    var activityList: ArrayList<Activity> = ArrayList()

    companion object {
        var myApp: MyApplication? = null
            private set
    }

    override fun onCreate() {
        super.onCreate()
        myApp = this
    }

    /**
     * 在新手机号验证成功后再销毁，否则要保留
     */
    var mBindNewPhoneActivity: BindNewPhoneActivity? = null

    /**
     * 在用户为新手机号点击发送验证码时再销毁之前创建的发验证码页面
     */
    var mReplacePhoneCodeActivity: ReplacePhoneCodeActivity? = null

    /**
     * 在用户答完问卷 回到首页的时候执行销毁申请页面
     */
    var mOpenIdentityActivity: OpenIdentityActivity? = null

    /**
     * 请求APP Token 后台约束参数
     */
    fun grantType(): String {
        val str = StringBuffer()
        str.append("grant_type")
        str.append("=")
        str.append("client_credentials")
        return str.toString()
    }

    fun closeApp() {
        for (activity: Activity in activityList) {
            activity.finish()
        }
        secretToken = null
    }

    fun getBody(string: String): RequestBody {
//        return string.toRequestBody("application/json;charset=utf-8".toMediaTypeOrNull())
        return string.toRequestBody("application/x-www-form-urlencoded; charset=utf-8".toMediaTypeOrNull())
    }
}