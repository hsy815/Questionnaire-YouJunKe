package com.biotecan.questionnaire.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import com.biotecan.questionnaire.R
import kotlinx.android.synthetic.main.loading_dialog.*

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.view
 * @创始人: hsy
 * @创建时间: 2020/5/13 9:26
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/13 9:26
 * @修改描述:
 */
class LoadingDialog(context: Context) : Dialog(context, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_dialog)
        setCanceledOnTouchOutside(false)
        val operatingAnim =
            AnimationUtils.loadAnimation(
                context,
                R.anim.loading_data
            )
        val lin = LinearInterpolator()
        operatingAnim!!.interpolator = lin
        loading_dialog_img.animation = operatingAnim
    }

}