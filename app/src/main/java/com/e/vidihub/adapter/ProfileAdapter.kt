package com.e.vidihub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.domain.model.UserModel
import com.e.vidihub.R

class ProfileAdapter(
    private val userModel: UserModel,
    private val context: Context
) : RecyclerView.Adapter<ProfileAdapter.ProfileViewHolder>() {

    inner class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val info: TextView = itemView.findViewById(R.id.tv_info)
        val icon: ImageView = itemView.findViewById(R.id.imageView2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_profile, parent, false)
        return ProfileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProfileViewHolder, position: Int) {

        Glide.with(context).load(context.getDrawable(R.drawable.ic_profile)).into(holder.icon)

        when (position) {

            0 -> {
                holder.title.text = "نوع اکانت"
                holder.info.text = userModel.accountType.toString()
            }

            1 -> {
                holder.title.text = "نام"
                holder.info.text = userModel.firstName.toString()
            }

            2 -> {
                holder.title.text = "نام خانوادگی"
                holder.info.text = userModel.lastName.toString()
            }

            3 -> {
                holder.title.text = "ایمیل"
                holder.info.text = userModel.email.toString()
            }

            4 -> {
                holder.title.text = "شماره تلفن"
                holder.info.text = userModel.phone.toString()
            }

            5 -> {
                holder.title.text = "کد ملی"
                holder.info.text = userModel.nationalCode.toString()
            }

        }
    }

    override fun getItemCount() = 6


}