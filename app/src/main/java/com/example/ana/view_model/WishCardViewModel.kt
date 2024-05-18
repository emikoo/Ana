package com.example.ana.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ana.data.model.WishCard
import com.example.ana.service.FirebaseWishCardService
import kotlinx.coroutines.launch

class WishCardViewModel : ViewModel() {

    private val _wish = MutableLiveData<WishCard>()
    val wish: LiveData<WishCard> = _wish
    private val _sectionItems = MutableLiveData<List<WishCard>>()
    val sectionItems: LiveData<List<WishCard>> = _sectionItems
    private val _albumPictures = MutableLiveData<List<WishCard>>()
    val albumPictures: LiveData<List<WishCard>> = _albumPictures

    fun getSectionItems(uid: String, sectionName: String) {
        viewModelScope.launch {
            _sectionItems.value = FirebaseWishCardService.getSectionItems(uid, sectionName)
        }
    }

    fun getAlbumPictures() {
        viewModelScope.launch {
            _albumPictures.value = FirebaseWishCardService.getAlbumPictures()
        }
    }

    fun uploadPicture(uid: String, sectionName: String, imageId: String, image: String) {
        viewModelScope.launch {
            FirebaseWishCardService.uploadPicture(uid, sectionName, imageId, image)
        }
    }
}