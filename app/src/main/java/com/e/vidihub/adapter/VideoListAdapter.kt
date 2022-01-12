package com.e.vidihub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.domain.model.VideoListItemModel
import com.e.vidihub.R

class VideoListAdapter(
    private val videoList: MutableList<VideoListItemModel>,
    private val context: Context,
    private val activity: FragmentActivity
) :
    RecyclerView.Adapter<VideoListAdapter.VideosViewHolder>() {

    inner class VideosViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tv_title)
        val poster: ImageView = itemView.findViewById(R.id.img_video_poster)
        val item: LinearLayout = itemView.findViewById(R.id.video_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
        return VideosViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
//        holder.poster.scaleType = ImageView.ScaleType.FIT_XY

        holder.title.text = videoList[position].title
        holder.item.setOnClickListener {
            activity.getSharedPreferences("video link", Context.MODE_PRIVATE).edit().putString(
                "key", videoList[position].guid
            ).apply()

//            val bundle = Bundle().apply { putString("video link", }
            activity.findNavController(R.id.nav_host_fragment).navigate(R.id.playVideoActivity)
        }
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

    override fun getItemCount() = videoList.size


}