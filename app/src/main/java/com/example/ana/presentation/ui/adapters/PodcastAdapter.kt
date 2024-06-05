package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Podcast

interface PodcastInterface {
    fun onPodcastSelected(podcast: Podcast)
}

class PodcastAdapter(
    private val podcast: MutableList<Podcast>,
    private val selector: PodcastInterface
) :
    RecyclerView.Adapter<PodcastAdapter.PodcastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PodcastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_podcast, parent, false)
        return PodcastViewHolder(view)
    }

    override fun onBindViewHolder(holder: PodcastViewHolder, position: Int) {
        holder.bind(podcast[position])
        holder.itemView.setOnClickListener { selector.onPodcastSelected(podcast[position]) }
    }

    override fun getItemCount() = podcast.count()

    class PodcastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.title)
        private val author: TextView = itemView.findViewById(R.id.author)
        private val preview: ImageView = itemView.findViewById(R.id.preview)

        fun bind(podcast: Podcast) {
            name.text = podcast.name
            author.text = podcast.author
            Glide.with(preview)
                .load(podcast.preview)
                .placeholder(R.drawable.ic_logo)
                .into(preview)
        }
    }
}