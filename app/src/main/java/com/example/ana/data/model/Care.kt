package com.example.ana.data.model

import com.example.ana.R

data class Care(
    val id: Int,
    val title: String,
    val subtitle: String,
    val icon: Int
)

val careList: MutableList<Care> = mutableListOf<Care>().apply {
    add(Care(1, "Meditation", "Keep calm and do meditation", R.drawable.ic_infinity))
    add(Care(2, "Podcast", "Keep calm and do meditation", R.drawable.ic_podcast))
    add(Care(3, "Wishcard", "Keep calm and do meditation", R.drawable.ic_stars))
    add(Care(4, "Gratitude diary", "Keep calm and do meditation", R.drawable.ic_wish))
    add(Care(5, "Insights", "Keep calm and do meditation", R.drawable.ic_diamond))
}
