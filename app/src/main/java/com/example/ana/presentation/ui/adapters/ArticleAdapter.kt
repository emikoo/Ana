package com.example.ana.presentation.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ana.R
import com.example.ana.data.model.Advice

class ArticleAdapter(private val articles: MutableList<Advice>) :
    RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.bind(articles[position])
    }

    override fun getItemCount() = articles.count()

    class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val category: TextView = itemView.findViewById(R.id.category)
        private val image: ImageView = itemView.findViewById(R.id.picture)
        private val book: ImageButton = itemView.findViewById(R.id.book)
        private val read: TextView = itemView.findViewById(R.id.read)
        private val article: TextView = itemView.findViewById(R.id.article)
        var flag = false

        @SuppressLint("SetTextI18n")
        fun bind(advice: Advice) {
            title.text = advice.title
            category.text = advice.category
            Glide.with(image)
                .load(advice.picture)
                .placeholder(R.drawable.ic_logo)
                .into(image)
            read.setOnClickListener {
                read(advice.article)
            }
            book.setOnClickListener {
                read(advice.article)
            }
        }

        private fun read(adviceArticle: String) {
            if (!flag) {
                article.visibility = View.VISIBLE
                article.text = adviceArticle
                read.visibility = View.GONE
                flag = true
            } else {
                article.visibility = View.GONE
                read.visibility = View.VISIBLE
                read.setText(R.string.read)
                flag = false
            }
        }
    }
}