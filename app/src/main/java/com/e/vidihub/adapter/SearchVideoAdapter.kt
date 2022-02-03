package com.e.vidihub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.domain.model.VideoListItemModel
import com.e.vidihub.R

class SearchVideoAdapter(
    private val videoList: MutableList<VideoListItemModel>,
    private val activity: AppCompatActivity,
    private val context: Context
) : RecyclerView.Adapter<SearchVideoAdapter.SearchVideoViewHolder>() {

    inner class SearchVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val poster: ImageView = itemView.findViewById(R.id.img_video_poster)
        val layout: LinearLayout = itemView.findViewById(R.id.video_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVideoViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return SearchVideoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SearchVideoViewHolder, position: Int) {
        holder.layout.setOnClickListener {
            activity.getSharedPreferences("video link", Context.MODE_PRIVATE).edit().putString(
                "key", videoList[position].guid
            ).apply()
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.playVideoActivity)
        }
        holder.title.text = videoList[position].title
        Glide.with(context)
            .load(
                Uri.parse(videoList[position].thumbnail)
            ).into(holder.poster)
        holder.poster.scaleType = ImageView.ScaleType.FIT_XY

    }


    override fun getItemCount() = videoList.size


}