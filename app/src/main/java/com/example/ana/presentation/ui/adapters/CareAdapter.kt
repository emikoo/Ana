package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ana.R
import com.example.ana.data.model.Care
import com.example.ana.data.model.Child
import com.example.ana.data.model.careList
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

interface CareSelector {
    fun openMeditation()
    fun openPodcasts()
}
class CareAdapter(private val selector: CareSelector) : RecyclerView.Adapter<CareAdapter.CareViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CareViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_care, parent, false)
        return CareViewHolder(view)
    }

    override fun onBindViewHolder(holder: CareViewHolder, position: Int) {
        holder.bind(careList[position])
        holder.itemView.setOnClickListener {
            when(position) {
                0 -> selector.openMeditation()
                1 -> selector.openPodcasts()
            }
        }
    }

    override fun getItemCount() = careList.size

    class CareViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val subtitle: TextView = itemView.findViewById(R.id.subtitle)
        private val icon: ImageView = itemView.findViewById(R.id.icon)

        fun bind(care: Care) {
            title.text = care.title
            subtitle.text = care.subtitle
            icon.setBackgroundResource(care.icon)
        }
    }
}