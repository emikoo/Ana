package com.example.ana.service

import android.annotation.SuppressLint
import android.util.Log
import com.example.ana.data.model.Advice
import com.example.ana.data.model.Advice.Companion.toAdvice
import com.example.ana.presentation.extensions.calculateWeeksLived
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirebaseAdviceService {
    private const val TAG = "FirebaseAdviceService"

    @SuppressLint("StaticFieldLeak")
    private val db = FirebaseFirestore.getInstance()

    suspend fun getAgeOfChild(userId: String, childName: String): String {
        return try {
            val child = db.collection("users")
                .document(userId)
                .collection("children").document(childName).get().await().get("birthday").toString()
            child
        } catch (e: Exception) {
            Log.e(TAG, "Error getting getAgeOfChild", e)
            FirebaseCrashlytics.getInstance().log("Error getting getAgeOfChild")
            FirebaseCrashlytics.getInstance().setCustomKey("uid", userId)
            FirebaseCrashlytics.getInstance().recordException(e)
            ""
        }
    }

    suspend fun getArticlesByCategory(lang: String, categoryName: String): List<Advice> {
        return try {
            val articles = db.collection("advices").whereEqualTo("language", lang)
                .whereEqualTo("category", categoryName)
            articles.get()
                .await()
                .documents
                .mapNotNull { it.toAdvice() }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting Advices", e)
            FirebaseCrashlytics.getInstance().log("Error getting Advices")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }

    suspend fun getArticlesByCategoryAndChildBirthdate(
        categoryName: String,
        birthdate: String
    ): List<Advice> {
        return try {
            val articles = db.collection("advices")
                .whereEqualTo("category", categoryName)
                .get()
                .await()
                .documents
                .mapNotNull { it.toAdvice() }

            val childWeeks = birthdate.calculateWeeksLived()

            articles.filter { advice ->
                val targetWeeks = advice.ageWeeks.toDouble()
                val minWeeks = targetWeeks - 5.0
                val maxWeeks = targetWeeks + 5.0
                childWeeks in minWeeks..maxWeeks
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error getting Advices", e)
            FirebaseCrashlytics.getInstance().log("Error getting Advices")
            FirebaseCrashlytics.getInstance().recordException(e)
            emptyList()
        }
    }
}