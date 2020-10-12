package com.biotecan.questionnaire.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.entity.SchemeInstances

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.adapter
 * @创始人: hsy
 * @创建时间: 2020/4/7 16:31
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/7 16:31
 * @修改描述:
 */
class MainButtonAdapter(
    var content: Context,
    private var onItemListener: OnItemListener
) :
    RecyclerView.Adapter<MainButtonAdapter.ViewHolder>() {

    private var buttonList: ArrayList<SchemeInstances> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(content).inflate(R.layout.main_button_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return buttonList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mainButton = buttonList[position]
        holder.buttonImg.setImageResource(R.mipmap.test_icon)
        holder.buttonText.text = mainButton.TypeName
        holder.buttonLin.setOnClickListener {
            onItemListener.mButton(mainButton)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val buttonImg: ImageView = view.findViewById(R.id.button_item_img)
        val buttonText: TextView = view.findViewById(R.id.button_item_text)
        val buttonLin: LinearLayout = view.findViewById(R.id.button_item_lin)
    }

    fun setButtonList(mButtonList: ArrayList<SchemeInstances>) {
        this.buttonList = mButtonList
        notifyDataSetChanged()
    }

    interface OnItemListener {
        fun mButton(mainButton: SchemeInstances)
    }

    fun setOnItemListener(mOnItemListener: OnItemListener) {
        this.onItemListener = mOnItemListener
    }
}