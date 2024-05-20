package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.SoundSession

interface SoundPlayer {
    fun soundPlay(soundUrl: String)
}

class SoundSessionAdapter(private val soundList: MutableList<SoundSession>, val player: SoundPlayer):  RecyclerView.Adapter<SoundSessionAdapter.SoundViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoundViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_sound, parent, false)
        return SoundViewHolder(view)
    }

    override fun onBindViewHolder(holder: SoundViewHolder, position: Int) {
        holder.bind(soundList[position], player)
        holder.itemView.setOnClickListener {
            player.soundPlay("https://firebasestorage.googleapis.com/v0/b/anake-5a71b.appspot.com/o/meditation%2Fâ%C2%9C¨%20Daily%2010%20Minute%20Meditation%20Music%20(No%20Talking)%20%5BHD%20Sound%5D%20â%C2%9C¨%20(128%20kbps).mp3?alt=media&token=6771999b-6948-4bc9-a74b-bcfe4a5784e5")
        }
    }

    override fun getItemCount() = soundList.size

    class SoundViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.title)
        private val preview: ImageView = itemView.findViewById(R.id.preview)
        private val ibPlayer: ImageButton = itemView.findViewById(R.id.ib_player)
        private val duration: TextView = itemView.findViewById(R.id.duration)
        private var flag: Boolean = false

        fun bind(soundSession: SoundSession, player: SoundPlayer) {
            name.text = soundSession.name
            Glide.with(preview)
                .load(soundSession.preview)
                .placeholder(R.drawable.ic_logo)
                .into(preview)
            val time = soundSession.duration.splitToSequence(".")
            if (time.count() == 1) duration.text = time.first() + " " + itemView.context.getString(R.string.min)
            else duration.text = time.first() + " " + itemView.context.getString(R.string.min) + " "+ time.last() + " " + itemView.context.getString(
                            R.string.sec)
            ibPlayer.setOnClickListener {
                if (!flag) {
                    flag = true
                    ibPlayer.setBackgroundResource(R.drawable.ic_pause_sharp)
                    player.soundPlay(soundSession.url)
                } else {
                    flag = false
                    ibPlayer.setBackgroundResource(R.drawable.ic_play_sharp)
                }
            }
        }
    }
}