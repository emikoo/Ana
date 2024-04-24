package com.example.ana.presentation.ui.fragments.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ana.R

class AdviceAdapter(private val count: Int) : RecyclerView.Adapter<AdviceAdapter.AdviceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdviceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_advice, parent, false)
        return AdviceViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdviceViewHolder, position: Int) {
    }

    override fun getItemCount() = count

    class AdviceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Инициализация элемента
    }
}
