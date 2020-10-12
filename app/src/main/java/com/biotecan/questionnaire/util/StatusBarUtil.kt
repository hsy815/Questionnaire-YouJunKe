package com.biotecan.questionnaire.util

import android.app.Activity
import android.view.View

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.utli
 * @创始人: hsy
 * @创建时间: 2020/4/7 10:32
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/7 10:32
 * @修改描述:
 */
object StatusBarUtil {

    val TYPE_MIUI = 1
    val TYPE_FLYME = 2
    val TYPE_M = 3

    /**
     * 改变状态栏字体颜色反转
     */
    fun setBarUI(activity: Activity, dark: Boolean) {
        val decorView = activity.window.decorView
        var vis = decorView.systemUiVisibility
        vis = if (dark) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        if (decorView.systemUiVisibility != vis) {
            decorView.systemUiVisibility = vis
        }

    }
/*
    fun setStatusBarDarkTheme(activity: Activity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            setStatusBarFontIconDark(activity, TYPE_M, dark)
        } else if (OSUtils.isMiui()) {
            setStatusBarFontIconDark(activity, TYPE_MIUI, dark)
        } else if (OSUtils.isFlyme()) {
            setStatusBarFontIconDark(activity, TYPE_FLYME, dark)
        } else { //其他情况
            return false
        }
        return false
    }

    fun setStatusBarFontIconDark(activity: Activity, type: Int, dark: Boolean): Boolean {
        when (type) {
            TYPE_MIUI -> {
                return setMiuiUI(activity, dark)
            }
            TYPE_FLYME -> {
                return setFlymeUI(activity, dark)
            }
            TYPE_M -> {
                return setCommonUI(activity, dark)
            }
        }
        return false
    }

    fun setCommonUI(activity: Activity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            var vis = decorView.systemUiVisibility
            vis = if (dark) {
                vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            } else {
                vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
            if (decorView.systemUiVisibility != vis) {
                decorView.systemUiVisibility = vis
            }
            return true
        }
        return false
    }

    //设置Flyme 状态栏深色浅色切换
    fun setFlymeUI(activity: Activity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag =
                WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags =
                WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //设置MIUI 状态栏深色浅色切换
    fun setMiuiUI(activity: Activity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val clazz: Class<*> = activity.window.javaClass
            @SuppressLint("PrivateApi") val layoutParams =
                Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field =
                layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getDeclaredMethod(
                "setExtraFlags",
                Int::class.javaPrimitiveType,
                Int::class.javaPrimitiveType
            )
            extraFlagField.isAccessible = true
            if (dark) { //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }*/
}