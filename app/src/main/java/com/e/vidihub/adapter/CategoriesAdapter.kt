package com.e.vidihub.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.domain.model.CategoryResponseModel
import com.e.vidihub.R


class CategoriesAdapter(
    private val categoriesList: MutableList<CategoryResponseModel>,
    private val onCategoriesListener: OnCategoriesListener
) : RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(
        itemView: View,
        private val onCategoriesListener: OnCategoriesListener
    ) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        val title: TextView = itemView.findViewById(R.id.tv_category_name)
//        val itemCategory: CardView = itemView.findViewById(R.id.item_category)

        init {
            title.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            onCategoriesListener.onCategoriesClick(absoluteAdapterPosition)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        return CategoriesViewHolder(itemView, onCategoriesListener)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        holder.title.text = categoriesList[position].name
    }

    override fun getItemCount() = categoriesList.size

}

//for categories item click listener
interface OnCategoriesListener {
    fun onCategoriesClick(position: Int)
}