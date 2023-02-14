package com.mambo.poetree.android.presentation.screens.home.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mambo.poetree.data.remote.dto.TopicDto
import com.mambo.poetree.data.repositories.PoemRepository
import com.mambo.poetree.data.repositories.TopicsRepository
import kotlinx.coroutines.launch

/**
 * @project Poetree
 * @author mambobryan
 * @email mambobryan@gmail.com
 * Mon 13 Feb 2023
 */
class SearchViewModel : ViewModel() {

    val topicsRepository = TopicsRepository()
    val poemRepository = PoemRepository()

    var topics by mutableStateOf<List<TopicDto>>(emptyList())

    init {
        viewModelScope.launch {
            val response = topicsRepository.getTopics(page = 0)
            if(response.isSuccessful){
                topics = response.data?.list ?: emptyList()
            }

        }
    }

}