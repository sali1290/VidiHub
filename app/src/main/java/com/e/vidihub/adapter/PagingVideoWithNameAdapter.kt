package com.e.vidihub.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.domain.model.VideoListItemModel
import com.e.vidihub.R

class PagingVideoWithNameAdapter(
    private val activity: FragmentActivity,
    private val context: Context,
    private val name: String
) :
    PagingDataAdapter<VideoListItemModel, RecyclerView.ViewHolder>(
        REPO_COMPARATOR
    ) {
    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<VideoListItemModel>() {
            override fun areItemsTheSame(oldItem: VideoListItemModel, newItem: VideoListItemModel) =
                oldItem == newItem

            override fun areContentsTheSame(
                oldItem: VideoListItemModel,
                newItem: VideoListItemModel
            ) = oldItem == newItem
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? PagingVideoViewHolder)?.bind(item = getItem(position))

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.item_video, parent, false)
        return PagingVideoViewHolder(itemView)
    }

    /**
     * view holder class
     */
    inner class PagingVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.tv_title)
        val poster: ImageView = itemView.findViewById(R.id.img_video_poster)
        val layout: LinearLayout = itemView.findViewById(R.id.video_item)

        fun bind(item: VideoListItemModel?) {
            //loads image from network using coil extension function
            if (name == item!!.title) {
                layout.setOnClickListener {
                    activity.getSharedPreferences("video link", Context.MODE_PRIVATE).edit()
                        .putString(
                            "key", item.guid
                        ).apply()
                    activity.findNavController(R.id.nav_host_fragment)
                        .navigate(R.id.playVideoActivity)
                }
                title.text = item.title
                Glide.with(context)
                    .load(
                        Uri.parse(item.thumbnail)
                    ).into(poster)
                poster.scaleType = ImageView.ScaleType.FIT_XY
            }
            else {
                layout.visibility = View.GONE
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateData() {
        notifyDataSetChanged()
    }


}