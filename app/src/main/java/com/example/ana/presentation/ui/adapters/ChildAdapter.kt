package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Child
import de.hdodenhof.circleimageview.CircleImageView
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class ChildAdapter(private val children: MutableList<Child>) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_child, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(children[position])
    }

    override fun getItemCount() = children.size

    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.name)
        private val age: TextView = itemView.findViewById(R.id.age)
        private val image: CircleImageView = itemView.findViewById(R.id.image)

        fun bind(child: Child) {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val birthDate = LocalDate.parse(child.birthday, formatter)
            val currentDate = LocalDate.now()
            val birthday = Period.between(birthDate, currentDate)
            age.text = "${birthday.years} year, ${birthday.months} months, ${birthday.days} days"
            name.text = child.name
            Glide.with(image)
                .load(child.image)
                .placeholder(R.drawable.ic_baby)
                .into(image)
        }
    }
}