package com.e.vidihub.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.e.domain.model.VideoListItemModel
import com.e.vidihub.R
import com.e.vidihub.activity.PlayVideoActivity

class PagingVideoAdapter(
    private val activity: FragmentActivity,
    private val context: Context
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
//                activity.getSharedPreferences("video link", Context.MODE_PRIVATE).edit().putString(
//                    "key", item!!.guid
//                ).apply()
//                activity.findNavController(R.id.nav_host_fragment)
//                    .navigate(R.id.playVideoActivity)
                val intent = Intent(activity, PlayVideoActivity::class.java)
                intent.putExtra("video link", item!!.guid)
                activity.startActivity(intent)
            }
            title.text = item!!.title
            Glide.with(context)
                .load(
                    Uri.parse(item.thumbnail)
                ).transition(DrawableTransitionOptions.withCrossFade())
                .error(R.drawable.icon_video).listener(
                    object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            poster.setColorFilter(Color.argb(255, 0, 151, 167))
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            return false
                        }

                    }
                ).into(poster)
            poster.scaleType = ImageView.ScaleType.FIT_XY

        }
    }

}