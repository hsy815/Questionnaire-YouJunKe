package com.biotecan.questionnaire.adapter

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.biotecan.questionnaire.R
import com.biotecan.questionnaire.entity.NewsList
import com.biotecan.questionnaire.pre.PreApi
import com.bumptech.glide.Glide

/**
 * @项目名: Questionnaire
 * @类位置: com.biotecan.questionnaire.adapter
 * @创始人: hsy
 * @创建时间: 2020/4/8 9:27
 * @类描述:
 * @修改人: hsy
 * @修改时间: 2020/4/8 9:27
 * @修改描述:
 */
class MainNewAdapter(var mContext: Context, var mCount: Int) :
    RecyclerView.Adapter<MainNewAdapter.ViewHolder>() {

    private var mNewList: ArrayList<NewsList> = ArrayList()
    private var mOnItemListener: OnItemListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.main_new_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return if (mNewList.size <= mCount) {
            mNewList.size
        } else {
            mCount
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mainNew = mNewList[position]
        holder.newTitle.text = mainNew.Subject
        holder.newContent.text = mainNew.Intro
        holder.newTime.text = mainNew.Date
        if (!TextUtils.isEmpty(mainNew.ImageUrl)) {
            val str = mainNew.ImageUrl.substring(2)
            Glide.with(mContext).load("${PreApi.xmlUrl}${str}")
                .centerCrop()
                .into(holder.newImg)
        }
        holder.newLin.setOnClickListener {
            mOnItemListener?.mNew(mainNew)
        }
    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val newLin: LinearLayout = view.findViewById(R.id.main_new_item_lin)
        val newTitle: TextView = view.findViewById(R.id.main_new_item_title)
        val newContent: TextView = view.findViewById(R.id.main_new_item_content)
        val newTime: TextView = view.findViewById(R.id.main_new_item_time)
        val newImg: ImageView = view.findViewById(R.id.main_new_item_img)
    }

    fun setNewList(newList: List<NewsList>) {
        this.mNewList = newList as ArrayList<NewsList>
        notifyDataSetChanged()
    }

    fun addNewList(newList: List<NewsList>) {
        this.mNewList.addAll(newList)
        notifyDataSetChanged()
    }

    fun newsClear() {
        this.mNewList.clear()
    }

    interface OnItemListener {
        fun mNew(mainNew: NewsList)
    }

    fun setOnItemListener(onItemListener: OnItemListener) {
        this.mOnItemListener = onItemListener
    }
}