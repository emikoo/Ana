package com.example.ana.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.CATEGORIES_SECTION
import com.example.ana.data.model.GALLERY_SECTION
import com.example.ana.data.model.ITEM_SECTION
import com.example.ana.data.model.WishCard

interface WishCardSelector {
    fun onSectorSelected(sector: WishCard)
}

class WishCardAdapter(
    private var wishCardList: MutableList<WishCard>,
    private val selector: WishCardSelector
) : RecyclerView.Adapter<WishCardAdapter.WishCardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishCardViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wish, parent, false)
        return WishCardViewHolder(view)
    }

    override fun getItemCount() = wishCardList.size

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: WishCardViewHolder, position: Int) {
        holder.bind(wishCardList[position])
        holder.itemView.setOnClickListener {
            selector.onSectorSelected(wishCardList[position])
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(modelList: MutableList<WishCard>) {
        wishCardList = modelList
        notifyDataSetChanged()
    }

    class WishCardViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.title)
        private val icon: ImageView = itemView.findViewById(R.id.icon)
        private val image: ImageView = itemView.findViewById(R.id.image)
        private val placeholder: ImageView = itemView.findViewById(R.id.placeholder)

        fun bind(sector: WishCard) {
            when (sector.itemSection) {
                CATEGORIES_SECTION -> {
                    name.visibility = View.VISIBLE
                    name.text = sector.name
                    icon.visibility = View.VISIBLE
                    placeholder.visibility = View.GONE
                    image.visibility = View.GONE
                }
                ITEM_SECTION -> {
                    name.visibility = View.GONE
                    icon.visibility = View.GONE
                    if (sector.image.isNullOrBlank()) {
                        image.visibility = View.GONE
                        placeholder.visibility = View.VISIBLE
                    } else {
                        image.visibility = View.VISIBLE
                        placeholder.visibility = View.GONE
                        Glide.with(image)
                            .load(sector.image)
                            .into(image)
                    }
                }
                GALLERY_SECTION -> {
                    name.visibility = View.GONE
                    icon.visibility = View.GONE
                    placeholder.visibility = View.GONE
                    image.visibility = View.VISIBLE
                    Glide.with(image)
                        .load(sector.image)
                        .placeholder(R.drawable.ic_logo)
                        .into(image)
                }
            }
        }
    }
}