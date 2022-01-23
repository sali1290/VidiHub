package com.e.vidihub.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.e.domain.model.VideoListItemModel
import com.e.vidihub.R

class PagingVideoAdapter(
    private val activity: FragmentActivity
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
            layout.setOnClickListener {
                activity.getSharedPreferences("video link", Context.MODE_PRIVATE).edit().putString(
                    "key", item!!.guid
                ).apply()
                activity.findNavController(R.id.nav_host_fragment).navigate(R.id.playVideoActivity)
            }
            title.text = item!!.title

//            when (position) {
//                0 -> {
//                    Glide.with(context)
//                        .load(
//                            Uri.parse("https://static01.nyt.com/images/2017/09/15/arts/24movie-posters8/24movie-posters8-superJumbo.jpg")
//                        ).into(poster)
//                }
//
//                1 -> {
//                    Glide.with(context)
//                        .load(
//                            Uri.parse("https://www.companyfolders.com/blog/media/2017/07/the-silence-of-the-lambs.jpg")
//                        ).into(poster)
//                }
//
//                2 -> {
//                    Glide.with(context)
//                        .load(
//                            Uri.parse("https://wallpapercave.com/wp/wp8872702.jpg")
//                        ).into(poster)
//                }
//
//                3 -> {
//                    Glide.with(context)
//                        .load(
//                            Uri.parse("https://www.bestmoviesbyfarr.com/static-assets/blobs/images/movies/poster/2001/2AbFdmpvi6Z8AGrBY79JmnSGSOy.jpg")
//                        ).into(poster)
//                }
//
//            }


        }
    }

}