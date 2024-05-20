package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Meditation

interface MeditationInterface {
    fun onLockedSelected(meditation: Meditation)
    fun onUnlockedSelected(meditation: Meditation)
}

class MeditationAdapter(private val meditation: MutableList<Meditation>,
                        private val selector: MeditationInterface) :
    RecyclerView.Adapter<MeditationAdapter.MeditationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MeditationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
        return MeditationViewHolder(view)
    }

    override fun onBindViewHolder(holder: MeditationViewHolder, position: Int) {
        holder.bind(meditation[position])
        holder.itemView.setOnClickListener {
            if (!meditation[position].locked) selector.onUnlockedSelected(meditation[position])
            else selector.onLockedSelected(meditation[position])
        }
    }

    override fun getItemCount() = meditation.size

    class MeditationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.title)
        private val preview: ImageView = itemView.findViewById(R.id.preview)
        private val lock: ImageView = itemView.findViewById(R.id.lock)
        private val total_sessions: TextView = itemView.findViewById(R.id.total_sessions)
        private val sessions: TextView = itemView.findViewById(R.id.sessions)

        fun bind(meditation: Meditation) {
            name.text = meditation.name
            total_sessions.text = meditation.sessions.toString() + itemView.context.getString(R.string.sessions)
            sessions.text = "${meditation.count}/${meditation.sessions}"
            if (!meditation.locked) lock.visibility = View.GONE
            else lock.visibility = View.VISIBLE
            Glide.with(preview)
                .load(meditation.preview)
                .placeholder(R.drawable.ic_logo)
                .into(preview)
        }
    }
}