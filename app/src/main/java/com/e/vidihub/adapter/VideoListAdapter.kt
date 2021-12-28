package com.e.vidihub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.vidihub.R

class VideoListAdapter(private val kind: Int, private val context: Context) :
    RecyclerView.Adapter<VideoListAdapter.VideosViewHolder>() {

    inner class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val poster: ImageView = itemView.findViewById(R.id.img_video_poster)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
//        holder.poster.scaleType = ImageView.ScaleType.FIT_XY

        holder.title.text = "video ${position + 1}"
        when (position) {
            0 -> {
                Glide.with(context)
                    .load(
                        Uri.parse("https://static01.nyt.com/images/2017/09/15/arts/24movie-posters8/24movie-posters8-superJumbo.jpg")
                    ).into(holder.poster)
            }

            1 -> {
                Glide.with(context)
                    .load(
                        Uri.parse("https://www.companyfolders.com/blog/media/2017/07/the-silence-of-the-lambs.jpg")
                    ).into(holder.poster)
            }

            2 -> {
                Glide.with(context)
                    .load(
                        Uri.parse("https://wallpapercave.com/wp/wp8872702.jpg")
                    ).into(holder.poster)
            }

            3 -> {
                Glide.with(context)
                    .load(
                        Uri.parse("https://www.bestmoviesbyfarr.com/static-assets/blobs/images/movies/poster/2001/2AbFdmpvi6Z8AGrBY79JmnSGSOy.jpg")
                    ).into(holder.poster)
            }

        }
    }

    override fun getItemCount(): Int {
        return kind
    }


}