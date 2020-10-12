package com.biotecan.questionnaire.pre

import com.biotecan.questionnaire.entity.SchemeInstances
import com.biotecan.questionnaire.entity.UserInfo
import com.biotecan.questionnaire.entity.UserSetting
import java.math.BigDecimal

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.pre
 * @创始人: hsy
 * @创建时间: 2020/4/7 16:56
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/7 16:56
 * @修改描述:
 */
object PreData {

    fun getUserSetting(userInfo: UserInfo): ArrayList<UserSetting> {
        val sex = if (userInfo.Gender == 1) "男性" else if (userInfo.Gender == 2) "女性" else "未知"
        val list = ArrayList<UserSetting>()
        list.add(UserSetting("头像", 1, "HeadPortrait", userInfo.HeadPortrait, "", 0))
        list.add(UserSetting("姓名", 0, "Name", userInfo.Name, "", 1))
        list.add(UserSetting("性别", 2, "Gender", sex, "", 1))
        list.add(UserSetting("手机号", 3, "MobilePhone", userInfo.MobilePhone, "", 3))

        list.add(
            UserSetting(
                "出生日期",
                2,
                "DateOfBirth",
                userInfo.DateOfBirth.toString().substring(0, userInfo.DateOfBirth.indexOf(" ")),
                "",
                1
            )
        )
        list.add(
            UserSetting(
                "身高(厘米)",
                0,
                "Stature",
                userInfo.Stature.setScale(2, BigDecimal.ROUND_DOWN).toString(),
                "",
                2
            )
        )
        list.add(
            UserSetting(
                "体重(公斤)",
                0,
                "BodyWeight",
                userInfo.BodyWeight.setScale(2, BigDecimal.ROUND_DOWN).toString(),
                "",
                2
            )
        )
        list.add(UserSetting("电子邮箱", 0, "Email", userInfo.Email, "", 1))
        list.add(UserSetting("民族", 0, "Folk", userInfo.Folk, "", 1))
        list.add(UserSetting("籍贯", 0, "NativePlace", userInfo.NativePlace, "", 1))
        list.add(UserSetting("联系地址", 0, "ContactAddress", userInfo.ContactAddress, "", 1))
        list.add(UserSetting("教育程度", 2, "Education", userInfo.Education, "", 1))
        list.add(UserSetting("职业", 2, "JobStatus", userInfo.JobStatus, "", 1))
        list.add(
            UserSetting(
                "家庭年收入",
                2,
                "AnnualHouseholdIncome",
                userInfo.AnnualHouseholdIncome,
                "",
                1
            )
        )
        list.add(UserSetting("", 3, "", "", "", 1))
        return list
    }

    fun getUserInfo(): UserInfo {
        return UserInfo(
            "",
            "",
            "",
            0,
            0,
            "",
            BigDecimal(0),
            BigDecimal(0),
            "",
            ArrayList<SchemeInstances>(),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            false,
            "",
            ""
        )
    }

    /**
     * 返回选择性别列
     */
    fun getGender(): Array<String> {
        return arrayOf("未知", "男性", "女性")
    }

    /**
     * 学历列
     */
    fun getEducation(): Array<String> {
        return arrayOf("大学/研究生及以上", "高中(含中专中职)", "初中及以下")
    }

    /**
     * 职业列
     */
    fun getJobStatus(): Array<String> {
        return arrayOf("在职", "自由职业", "学生(含休学)", "无业", "退休")
    }

    /**
     * 家庭年收入列
     */
    fun getAnnualHouseholdIncome(): Array<String> {
        return arrayOf("1万以下", "1-5万", "5-20万", "20万以上")
    }
}