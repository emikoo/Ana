package com.example.ana.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ana.data.model.Child
import com.example.ana.service.FirebaseHomeService
import com.example.ana.service.FirebaseMessagesService
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {
    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children

    fun getChildren(uid: String) {
        viewModelScope.launch {
            _children.value = FirebaseHomeService.getChildren(uid)
        }
    }

    fun addChild(uid: String, child: Child) {
        viewModelScope.launch {
            FirebaseHomeService.addChild(uid, child)
        }
    }
}