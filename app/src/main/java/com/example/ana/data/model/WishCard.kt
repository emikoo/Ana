package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class WishCard(
    val name: String? = null,
    val image: String? = null,
    val itemSection: Int = CATEGORIES_SECTION,
    val selected: Boolean = false
) {
    companion object {
        fun DocumentSnapshot.toWishCard(): WishCard? {
            return try {
                val name = getString("name").toString()
                val image = getString("image").toString()
                val itemSection = get("itemSection").toString().toInt()
                val selected = getBoolean("selected")
                WishCard(name, image, itemSection)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting WishCard", e)
                FirebaseCrashlytics.getInstance().log("Error converting WishCard")
                FirebaseCrashlytics.getInstance().setCustomKey("id", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "WishCard"
    }
}

var cardList = mutableListOf<WishCard>().apply {
    add(WishCard("Finance"))
    add(WishCard("Travel"))
    add(WishCard("Education"))
    add(WishCard("Family"))
    add(WishCard("Me"))
    add(WishCard("Creativity"))
    add(WishCard("Success"))
    add(WishCard("Career"))
    add(WishCard("Hobby"))
}

var cardImagesList = mutableListOf<WishCard>().apply {
    add(WishCard("1", "", ITEM_SECTION))
    add(WishCard("2", "", ITEM_SECTION))
    add(WishCard("3", "", ITEM_SECTION))
    add(WishCard("4", "", ITEM_SECTION))
    add(WishCard("5", "", ITEM_SECTION))
    add(WishCard("6", "", ITEM_SECTION))
}

const val CATEGORIES_SECTION = 1
const val ITEM_SECTION = 2
const val GALLERY_SECTION = 3
