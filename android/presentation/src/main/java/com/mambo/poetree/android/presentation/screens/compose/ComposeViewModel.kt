package com.mambo.poetree.android.presentation.screens.compose

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.darkrockstudios.richtexteditor.model.RichTextValue
import com.mambo.poetree.AppMonitor.hideLoading
import com.mambo.poetree.AppMonitor.showDialog
import com.mambo.poetree.AppMonitor.showLoading
import com.mambo.poetree.data.domain.Poem
import com.mambo.poetree.data.domain.Topic
import com.mambo.poetree.data.repositories.PoemRepository
import com.mambo.poetree.data.repositories.TopicsRepository
import com.mambo.poetree.utils.DialogData
import com.mambo.poetree.utils.DialogType
import kotlinx.coroutines.launch

/**
 * @project Poetree
 * @author mambobryan
 * @email mambobryan@gmail.com
 * Wed 15 Feb 2023
 */
class ComposeViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val poemRepository = PoemRepository()
    private val topicRepository = TopicsRepository()

    private var poem by mutableStateOf<Poem?>(null)

    var topics by mutableStateOf<List<Topic>>(emptyList())

    var topic by mutableStateOf<Topic?>(null)
        private set

    var title by mutableStateOf("")
        private set

    var content by mutableStateOf(RichTextValue.get())
        private set

    var isButtonEnabled by mutableStateOf(getIsButtonEnabled())
        private set

    var isValidPoem by mutableStateOf(getValidPoemDetails())
        private set

    init {
        viewModelScope.launch {
            val response = topicRepository.getTopics(page = 1)
            if (response.isSuccessful) {
                topics = response.data?.list?.map { it.toDomain() } ?: listOf()
            }
        }
    }

    private fun getIsButtonEnabled() = when (poem != null) {
        true -> (poem?.title != title) and (poem?.content != content.value.text) and (poem?.topic != topic)
        else -> title.isNotBlank() and content.value.text.isNotBlank()
    }

    private fun getValidPoemDetails() = when (poem != null) {
        true -> (poem?.title?.isNotBlank() == true) and (poem?.content?.isNotBlank() == true) and (poem?.topic != null)
        else -> title.isNotBlank() and content.value.text.isNotBlank() and (topic != null)
    }

    private fun updateStates() {
        isButtonEnabled = getIsButtonEnabled()
        isValidPoem = getValidPoemDetails()
    }

    fun updatePoem(poem: Poem) {
        this.poem = poem
        topic = poem.topic
        title = poem.title
        content = RichTextValue.fromString(text = poem.content)
        updateStates()
    }

    fun updateTopic(topic: Topic) {
        this.topic = topic
    }

    fun updateTitle(text: String) {
        title = text
        updateStates()

    }

    fun updateContent(value: RichTextValue) {
        content = value
        updateStates()

    }

    fun save(onSuccess: (Poem) -> Unit, onError: () -> Unit) {
        when (poem == null) {
            true -> createPoem(onSuccess, onError)
            false -> updatePoem()
        }
    }

    fun publish() {}

    fun delete() {}

    private fun createPoem(onSuccess: (Poem) -> Unit, onError: () -> Unit) {
        viewModelScope.launch {
            showLoading()
            val poem =
                poemRepository.saveDraft(title = title, content = content.value.text, topic = topic)
            hideLoading()
            when (poem == null) {
                true -> {
                    showDialog(
                        data = DialogData(
                            type = DialogType.ERROR,
                            title = "Error",
                            description = "Couldn't save poem"
                        )
                    )
                }
                false -> {
                    showDialog(
                        data = DialogData(
                            type = DialogType.SUCCESS,
                            title = "Success",
                            description = "Saved poem successfully",
                            positiveAction = { onSuccess(poem) }
                        )
                    )
                }
            }

        }
    }

    private fun updatePoem() {}

}