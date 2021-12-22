package com.e.vidihub.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.e.vidihub.R

class HomeViewPagerAdapter(private val context: Context) :
    RecyclerView.Adapter<HomeViewPagerAdapter.HomeViewPagerViewHolder>() {

    inner class HomeViewPagerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.img_home)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewPagerViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return HomeViewPagerViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: HomeViewPagerViewHolder, position: Int) {
        when (position) {

            0 -> {
                holder.image.scaleType = ImageView.ScaleType.FIT_XY
                Glide.with(context)
                    .load(
                        Uri.parse("https://static01.nyt.com/images/2017/09/15/arts/24movie-posters8/24movie-posters8-superJumbo.jpg")
                    ).into(holder.image)
            }

            1 -> {
                holder.image.scaleType = ImageView.ScaleType.FIT_XY
                Glide.with(context)
                    .load(
                        Uri.parse("https://www.companyfolders.com/blog/media/2017/07/the-silence-of-the-lambs.jpg")
                    ).into(holder.image)
            }

            2 -> {
                holder.image.scaleType = ImageView.ScaleType.FIT_XY
                Glide.with(context)
                    .load(
                        Uri.parse("https://wallpapercave.com/wp/wp8872702.jpg")
                    ).into(holder.image)
            }

        }
    }

    override fun getItemCount() = 3

}