package com.example.ana.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ana.data.model.Advice
import com.example.ana.data.model.Child
import com.example.ana.data.model.User
import com.example.ana.service.FirebaseAdviceService
import com.example.ana.service.FirebaseHomeService
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel() {
    private val _children = MutableLiveData<List<Child>>()
    val children: LiveData<List<Child>> = _children
    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name
    private val _photo = MutableLiveData<String>()
    val photo: LiveData<String> = _photo
    private val _advices = MutableLiveData<MutableList<Advice>>()
    val advices: LiveData<MutableList<Advice>> = _advices

    fun getUserName(docId: String) {
        viewModelScope.launch {
            _name.value = FirebaseHomeService.getUserName(docId)
        }
    }

    fun getProfilePhoto(docId: String) {
        viewModelScope.launch {
            _photo.value = FirebaseHomeService.getProfilePhoto(docId)
        }
    }

    fun getChildren(uid: String) {
        viewModelScope.launch {
            _children.value = FirebaseHomeService.getChildren(uid)
        }
    }

    fun getAdvices(lang: String) {
        viewModelScope.launch {
            _advices.value = FirebaseHomeService.getAdvices(lang).toMutableList()
        }
    }

    fun addChild(uid: String, child: Child) {
        viewModelScope.launch {
            FirebaseHomeService.addChild(uid, child)
        }
    }

    fun deleteChild(uid: String, childName: String) {
        viewModelScope.launch {
            FirebaseHomeService.deleteChild(uid, childName)
        }
    }

    fun updateUser(uid: String, user: User) {
        viewModelScope.launch {
            FirebaseHomeService.updateUser(uid, user)
        }
    }
}