package com.example.ana.presentation.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Child
import com.example.ana.presentation.extensions.ageOfChild
import de.hdodenhof.circleimageview.CircleImageView
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

interface ChildSelector {
    fun onChildPressed(child: Child)
}
class ChildAdapter(private val children: MutableList<Child>, val selector: ChildSelector) : RecyclerView.Adapter<ChildAdapter.ChildViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_child, parent, false)
        return ChildViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChildViewHolder, position: Int) {
        holder.bind(children[position])
        holder.itemView.setOnClickListener { selector.onChildPressed(children[position]) }
    }

    override fun getItemCount() = children.size

    fun deleteChild(position: Int) {
        children.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    class ChildViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val name: TextView = itemView.findViewById(R.id.title)
        private val age: TextView = itemView.findViewById(R.id.age)
        private val image: CircleImageView = itemView.findViewById(R.id.image)

        fun bind(child: Child) {
            age.ageOfChild(child.birthday)
            name.text = child.name
            Glide.with(image)
                .load(child.image)
                .placeholder(R.drawable.ic_baby)
                .into(image)
        }
    }
}