package com.example.ana.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Advice

interface AdviceSelector {
    fun onAdvicePressed(advice: Advice)
}

class AdviceAdapter(private val articles: MutableList<Advice>, val selector: AdviceSelector) :
    RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advice, parent, false)
        return AdviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
        holder.bind(articles[position])
        holder.itemView.setOnClickListener { selector.onAdvicePressed(articles[position]) }
    }

    override fun getItemCount() = articles.size

    class AdviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val ageDate: TextView = itemView.findViewById(R.id.age_date)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val image: ImageView = itemView.findViewById(R.id.image)

        @SuppressLint("SetTextI18n")
        fun bind(advice: Advice) {
            title.text = advice.title
            ageDate.text = advice.created
            category.text = advice.category
            Glide.with(image)
                .load(advice.picture)
                .placeholder(R.drawable.ic_logo)
                .into(image)
        }
    }
}
