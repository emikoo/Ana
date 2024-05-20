package com.example.ana.data.model

import com.example.ana.R

data class Care(
    val id: Int,
    val title: Int,
    val subtitle: Int,
    val icon: Int
)

//val careList: MutableList<Care> = mutableListOf<Care>().apply {
//    add(Care(1, Resources.getSystem().getString(R.string.meditation), Resources.getSystem().getString(R.string.keep_calm_and_do_meditation), R.drawable.ic_infinity))
//    add(Care(2, Resources.getSystem().getString(R.string.podcast), Resources.getSystem().getString(R.string.keep_calm_and_do_meditation), R.drawable.ic_podcast))
//    add(Care(3, Resources.getSystem().getString(R.string.wishcard), Resources.getSystem().getString(R.string.keep_calm_and_do_meditation), R.drawable.ic_stars))
//    add(Care(4, Resources.getSystem().getString(R.string.gratitude_diary), Resources.getSystem().getString(R.string.keep_calm_and_do_meditation), R.drawable.ic_wish))
//    add(Care(5, Resources.getSystem().getString(R.string.insights), Resources.getSystem().getString(R.string.keep_calm_and_do_meditation), R.drawable.ic_diamond))
//}

//val careList: MutableList<Care> = mutableListOf<Care>().apply {
//    add(Care(1, "Meditation", "Keep calm and do meditation", R.drawable.ic_infinity))
//    add(Care(2, "Podcast", "Keep calm and do meditation", R.drawable.ic_podcast))
//    add(Care(3, "Wishcard", "Keep calm and do meditation", R.drawable.ic_stars))
//    add(Care(4, "Gratitude diary", "Keep calm and do meditation", R.drawable.ic_wish))
//    add(Care(5, "Insights", "Keep calm and do meditation", R.drawable.ic_diamond))
//}

val careList: MutableList<Care> = mutableListOf<Care>().apply {
    add(Care(1, R.string.meditation, R.string.keep_calm_and_do_meditation, R.drawable.ic_infinity))
    add(Care(2, R.string.podcast, R.string.keep_calm_and_do_meditation, R.drawable.ic_podcast))
    add(Care(3, R.string.wishcard, R.string.keep_calm_and_do_meditation, R.drawable.ic_stars))
    add(Care(4, R.string.gratitude_diary, R.string.keep_calm_and_do_meditation, R.drawable.ic_wish))
    add(Care(5, R.string.insights, R.string.keep_calm_and_do_meditation, R.drawable.ic_diamond))
}