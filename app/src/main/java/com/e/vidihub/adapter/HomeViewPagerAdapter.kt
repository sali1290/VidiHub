package com.e.vidihub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.domain.model.VideoPosterModel
import com.e.vidihub.R

class HomeViewPagerAdapter(
    private val videosList: MutableList<VideoPosterModel>,
    private val context: Context,
    private val onPlayClickListener: OnPlayClickListener
) :
    RecyclerView.Adapter<HomeViewPagerAdapter.HomeViewPagerViewHolder>() {

    inner class HomeViewPagerViewHolder(
        itemView: View,
        private val onPlayClickListener: OnPlayClickListener
    ) : RecyclerView.ViewHolder(itemView) , View.OnClickListener {
        val image: ImageView = itemView.findViewById(R.id.img_home)
        val watchVideo: CardView = itemView.findViewById(R.id.card_watch_video)

        init {
            watchVideo.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            onPlayClickListener.onPlayClick(absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewPagerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return HomeViewPagerViewHolder(itemView , onPlayClickListener)
    }

    override fun onBindViewHolder(holder: HomeViewPagerViewHolder, position: Int) {
        holder.image.scaleType = ImageView.ScaleType.FIT_XY
        Glide.with(context)
            .load(
                Uri.parse(videosList[position].posterLink)
            ).into(holder.image)


//        when (position) {
//
//            0 -> {
//                holder.image.scaleType = ImageView.ScaleType.FIT_XY
//                Glide.with(context)
//                    .load(
//                        Uri.parse("https://static01.nyt.com/images/2017/09/15/arts/24movie-posters8/24movie-posters8-superJumbo.jpg")
//                    ).into(holder.image)
//            }
//
//            1 -> {
//                holder.image.scaleType = ImageView.ScaleType.FIT_XY
//                Glide.with(context)
//                    .load(
//                        Uri.parse("https://www.companyfolders.com/blog/media/2017/07/the-silence-of-the-lambs.jpg")
//                    ).into(holder.image)
//            }
//
//            2 -> {
//                holder.image.scaleType = ImageView.ScaleType.FIT_XY
//                Glide.with(context)
//                    .load(
//                        Uri.parse("https://wallpapercave.com/wp/wp8872702.jpg")
//                    ).into(holder.image)
//            }
//
//        }
    }

    override fun getItemCount() = videosList.size

    interface OnPlayClickListener {
        fun onPlayClick(position: Int)
    }

}