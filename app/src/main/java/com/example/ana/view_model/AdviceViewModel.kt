package com.example.ana.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ana.data.model.Advice
import com.example.ana.service.FirebaseAdviceService
import kotlinx.coroutines.launch

class AdviceViewModel : ViewModel() {
    private val _ageOfChild = MutableLiveData<String>()
    val ageOfChild: LiveData<String> = _ageOfChild
    private val _articlesByChild = MutableLiveData<List<Advice>>()
    val articlesByChild: LiveData<List<Advice>> = _articlesByChild
    private val _articles = MutableLiveData<List<Advice>>()
    val articles: LiveData<List<Advice>> = _articles

    fun getAgeOfChild(userId: String, childName: String) {
        viewModelScope.launch {
            _ageOfChild.value = FirebaseAdviceService.getAgeOfChild(userId, childName)
        }
    }

    fun getArticlesByCategoryAndChildBirthdate(categoryName: String, birthdate: String) {
        viewModelScope.launch {
            _articlesByChild.value = FirebaseAdviceService.getArticlesByCategoryAndChildBirthdate(categoryName, birthdate)
        }
    }
    fun getArticlesByCategory(lang: String, categoryName: String) {
        viewModelScope.launch {
            _articlesByChild.value = FirebaseAdviceService.getArticlesByCategory(lang, categoryName)
        }
    }
}