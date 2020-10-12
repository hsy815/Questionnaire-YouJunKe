package com.biotecan.questionnaire.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.entity.FindAudience

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.adapter
 * @创始人: hsy
 * @创建时间: 2020/5/21 10:54
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/21 10:54
 * @修改描述:
 */
class SeeQuestionAdapter(private val context: Context) :
    RecyclerView.Adapter<SeeQuestionAdapter.ViewHolder>() {

    private var seeList = ArrayList<FindAudience>()
    private var mOnItemListener: OnItemListener? = null

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleText: TextView = view.findViewById(R.id.see_question_item_title)
        val timeText: TextView = view.findViewById(R.id.see_question_item_time)
        val stateText: TextView = view.findViewById(R.id.see_question_item_state)
        val rel: RelativeLayout = view.findViewById(R.id.see_question_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.see_question_item, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return seeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val findAudience = seeList[position]
        holder.titleText.text = findAudience.Name
        holder.timeText.text = findAudience.RTime
        if (findAudience.NeedAssessment) {
            var str = ""
            when (findAudience.Assessment) {
                1 -> str = "评定通过"
                2 -> {
                    str = "评定拒绝"
                    holder.stateText.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.see_state_red
                        )
                    )
                }
                else -> str = "NA"
            }
            holder.stateText.text = str
        } else {
            holder.stateText.text = "已完成"
        }
        holder.rel.setOnClickListener {
            mOnItemListener?.item(findAudience)
        }
    }

    fun setSeeList(seeList: ArrayList<FindAudience>) {
        this.seeList = seeList
        notifyDataSetChanged()
    }

    fun setOnItemListener(onItemListener: OnItemListener) {
        this.mOnItemListener = onItemListener
    }

    interface OnItemListener {
        fun item(findAudience: FindAudience)
    }
}