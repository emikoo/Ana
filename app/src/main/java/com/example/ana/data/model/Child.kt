package com.example.ana.data.model

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentSnapshot

data class Child(
    val name: String,
    val birthday: String,
    val image: String? = null,
    val diary: MutableList<Diary>? = null
) {
    companion object {
        fun DocumentSnapshot.toChild(): Child? {
            return try {
                val name = getString("name")!!
                val birthday = getString("birthday")!!
                val image = getString("image")
//                val diary = get("diary") as Array<Diary>?
                Child(name, birthday, image)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting child", e)
                FirebaseCrashlytics.getInstance().log("Error converting child")
                FirebaseCrashlytics.getInstance().setCustomKey("uid", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "Child"
    }
}

data class Diary(
    val title: String,
    val notes: String,
    val time: String? = null
) {
    companion object {
        fun DocumentSnapshot.toDiary(): Diary? {
            return try {
                val title = getString("title")!!
                val notes = getString("notes")!!
                val time = getString("time")!!
                Diary(title, notes, time)
            } catch (e: Exception) {
                Log.e(TAG, "Error converting diary", e)
                FirebaseCrashlytics.getInstance().log("Error converting diary")
                FirebaseCrashlytics.getInstance().setCustomKey("uid", id)
                FirebaseCrashlytics.getInstance().recordException(e)
                null
            }
        }

        private const val TAG = "Child"
    }
}