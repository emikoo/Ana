package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ana.R
import com.example.ana.data.model.Care
import com.example.ana.data.model.careList

interface CareSelector {
    fun openMeditation()
    fun openPodcasts()

    fun openWishCard()
    fun premium()
}
class CareAdapter(private val selector: CareSelector) :
    RecyclerView.Adapter<CareAdapter.CareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_care, parent, false)
        return CareViewHolder(view)
    }

    override fun onBindViewHolder(holder: CareViewHolder, position: Int) {
        holder.bind(careList[position])
        holder.itemView.setOnClickListener {
            when (position) {
                0 -> selector.openMeditation()
                1 -> selector.openPodcasts()
                2 -> selector.openWishCard()
                3 -> selector.premium()
            }
        }
    }

    override fun getItemCount() = careList.size

    class CareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val subtitle: TextView = itemView.findViewById(R.id.subtitle)
        private val icon: ImageView = itemView.findViewById(R.id.icon)

        fun bind(care: Care) {
            title.text = itemView.context.getString(care.title)
            subtitle.text = itemView.context.getString(care.subtitle)
            icon.setBackgroundResource(care.icon)
        }
    }
}