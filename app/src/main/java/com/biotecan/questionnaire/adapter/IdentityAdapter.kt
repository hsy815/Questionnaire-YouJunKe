package com.biotecan.questionnaire.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.entity.Disease

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.adapter
 * @创始人: hsy
 * @创建时间: 2020/5/8 15:57
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/5/8 15:57
 * @修改描述:
 */
class IdentityAdapter(private val context: Context) :
    RecyclerView.Adapter<IdentityAdapter.SelectViewHolder>() {

    private var diseaseList: List<Disease> = ArrayList()
    private var mOnItemClick: OnItemClick? = null

    inner class SelectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textName: TextView = view.findViewById(R.id.identity_item_name)
        val textBtn: TextView = view.findViewById(R.id.identity_item_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectViewHolder {
        return SelectViewHolder(
            LayoutInflater.from(context).inflate(R.layout.identity_item_view, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return diseaseList.size
    }

    override fun onBindViewHolder(holder: SelectViewHolder, position: Int) {
        val disease = diseaseList[position]
        holder.textName.text = disease.Name
        if (disease.isSelect) {
            holder.textBtn.text = context.getString(R.string.identity_item_btn_no)
            holder.textBtn.background = context.getDrawable(R.drawable.identity_item_bg)
            holder.textBtn.setTextColor(ContextCompat.getColor(context, R.color.login_line_bg))
        } else {
            holder.textBtn.text = context.getString(R.string.identity_item_btn)
            holder.textBtn.background = context.getDrawable(R.drawable.main_input_text_bg)
            holder.textBtn.setTextColor(Color.WHITE)
        }
        holder.textBtn.setOnClickListener {
            if (!disease.isSelect)
                mOnItemClick?.item(disease)
        }
    }

    interface OnItemClick {
        fun item(disease: Disease)
    }

    fun setOnItemClick(onItemClick: OnItemClick) {
        this.mOnItemClick = onItemClick
    }

    fun setData(diseaseList: ArrayList<Disease>) {
        this.diseaseList = diseaseList
        notifyDataSetChanged()
    }
}