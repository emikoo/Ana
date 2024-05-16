package com.example.ana.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ana.data.model.Meditation
import com.example.ana.data.model.Podcast
import com.example.ana.service.FirebaseCareService
import kotlinx.coroutines.launch

class CareViewModel : ViewModel() {
    private val _meditation = MutableLiveData<List<Meditation>>()
    val meditation: LiveData<List<Meditation>> = _meditation
    private val _podcast = MutableLiveData<List<Podcast>>()
    val podcast: LiveData<List<Podcast>> = _podcast
    private val _podcastExcept = MutableLiveData<List<Podcast>>()
    val podcastExcept: LiveData<List<Podcast>> = _podcastExcept
    private val _itemPodcast = MutableLiveData<Podcast>()
    val itemPodcast: LiveData<Podcast> = _itemPodcast

    fun getMeditation() {
        viewModelScope.launch {
            _meditation.value = FirebaseCareService.getMeditation()
        }
    }

    fun getPodcast() {
        viewModelScope.launch {
            _podcast.value = FirebaseCareService.getPodcast()
        }
    }

    fun getPodcastExceptItem(itemId: Int) {
        viewModelScope.launch {
            _podcastExcept.value = FirebaseCareService.getPodcastExceptItem(itemId)
        }
    }

    fun getItemPodcast(itemId: Int) {
        viewModelScope.launch {
            _itemPodcast.value = FirebaseCareService.getItemPodcast(itemId)
        }
    }

    fun updateViews(podcastId: Int, views: Int) {
        viewModelScope.launch {
            FirebaseCareService.updateViews(podcastId, views)
        }
    }
}