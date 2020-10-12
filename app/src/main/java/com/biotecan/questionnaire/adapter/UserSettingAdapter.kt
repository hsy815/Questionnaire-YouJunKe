package com.biotecan.questionnaire.adapter

import android.content.Context
import android.text.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.entity.UserSetting
import com.biotecan.questionnaire.util.BitmapOrBase64
import com.biotecan.questionnaire.view.CircleImageView

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.adapter
 * @创始人: hsy
 * @创建时间: 2020/4/27 10:20
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/27 10:20
 * @修改描述:
 */
class UserSettingAdapter(var mContext: Context, var settingList: ArrayList<UserSetting>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val TYPE_EDIT: Int = 0
    val TYPE_HEAD: Int = 1
    val TYPE_SELECT: Int = 2
    val TYPE_TEXT: Int = 3

    private var mOnItemClick: OnItemClick? = null
    private var mOnHeadItemClick: OnHeadItemClick? = null

    override fun getItemViewType(position: Int): Int {
        return settingList[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            TYPE_HEAD -> {
                HeadViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.user_setting_head, parent, false)
                )
            }
            TYPE_EDIT -> {
                EditViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.user_setting_edit, parent, false)
                )
            }
            TYPE_SELECT -> {
                SelectViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.user_setting_select, parent, false)
                )
            }
            TYPE_TEXT -> {
                TextViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.user_setting_text, parent, false)
                )
            }
            else -> {
                OtherViewHolder(
                    LayoutInflater.from(mContext)
                        .inflate(R.layout.user_setting_other, parent, false)
                )
            }
        }

    override fun getItemCount(): Int {
        return settingList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val userSetting = settingList[position]
        when (holder) {
            is HeadViewHolder -> {
                holder.headTitle.text = userSetting.title
                holder.headRal.setOnClickListener {
                    mOnHeadItemClick?.itemData(userSetting, holder.headimg)
                }
                if (TextUtils.isEmpty(userSetting.value)) {
                    holder.headimg.setImageResource(R.mipmap.head)
                } else {
                    val bitmap = BitmapOrBase64.base64ToBitmap(userSetting.value!!)
                    holder.headimg.setImageBitmap(bitmap)
                }
            }
            is TextViewHolder -> {
                holder.textTitle.text = userSetting.title
                holder.textEd.text = userSetting.value
            }
            is EditViewHolder -> {
                holder.editTitle.text = userSetting.title
                setEditInputType(holder.editEd, userSetting.editType)
                holder.editEd.tag = userSetting.title
                holder.editEd.setText(userSetting.value)
                val textWatcher = object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        userSetting.temporaryValue = s.toString()
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence?,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }
                }
                holder.editEd.setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        holder.editEd.addTextChangedListener(textWatcher)
                    } else {
                        holder.editEd.removeTextChangedListener(textWatcher)
                    }
                }
            }
            is SelectViewHolder -> {
                holder.selectTitle.text = userSetting.title
                holder.selectEd.text = userSetting.value
                holder.selectRal.setOnClickListener {
                    mOnItemClick?.itemData(userSetting, holder.selectEd)
                }
            }
        }
    }

    internal inner class HeadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var headRal: RelativeLayout = view.findViewById(R.id.user_setting_head)
        var headTitle: TextView = view.findViewById(R.id.user_setting_head_text)
        var headimg: CircleImageView = view.findViewById(R.id.user_setting_head_img)
    }

    internal inner class TextViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textRal: RelativeLayout = view.findViewById(R.id.user_setting_text)
        var textTitle: TextView = view.findViewById(R.id.user_setting_text_text)
        var textEd: TextView = view.findViewById(R.id.user_setting_text_ed)
    }

    internal inner class EditViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var editRal: RelativeLayout = view.findViewById(R.id.user_setting_edit)
        var editTitle: TextView = view.findViewById(R.id.user_setting_edit_text)
        var editEd: EditText = view.findViewById(R.id.user_setting_edit_ed)
    }

    internal inner class SelectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var selectRal: RelativeLayout = view.findViewById(R.id.user_setting_select)
        var selectTitle: TextView = view.findViewById(R.id.user_setting_select_text)
        var selectEd: TextView = view.findViewById(R.id.user_setting_select_ed)
    }

    class OtherViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }


    private fun setEditInputType(edt: EditText, type: Int) {
        when (type) {
            1 -> {
                edt.inputType = InputType.TYPE_CLASS_TEXT
                edt.filters = arrayOf(InputFilter.LengthFilter(10))
            }
            2 -> {
                edt.inputType = InputType.TYPE_CLASS_NUMBER
                edt.filters = arrayOf(InputFilter.LengthFilter(5))
            }
            3 -> {
                edt.inputType = InputType.TYPE_CLASS_PHONE
                edt.filters = arrayOf(InputFilter.LengthFilter(11))
            }
            else -> {
                edt.inputType = InputType.TYPE_CLASS_TEXT
                edt.filters = arrayOf(InputFilter.LengthFilter(1000))
            }
        }
    }

    interface OnItemClick {
        fun itemData(userSetting: UserSetting, textView: TextView)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.mOnItemClick = onItemClick
    }

    interface OnHeadItemClick {
        fun itemData(userSetting: UserSetting, imageView: CircleImageView)
    }

    fun setOnHeadItemClick(onHeadItemClick: OnHeadItemClick) {
        this.mOnHeadItemClick = onHeadItemClick
    }
}