package com.example.sampleappgpt3.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleappgpt3.ChatRoomsUiState
import com.example.sampleappgpt3.data.ChatRepository
import com.example.sampleappgpt3.data.datastore.entity.Chat
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatRoomsViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    val chatRooms: StateFlow<ChatRoomsUiState> = chatRepository.getChats()
        .map(ChatRoomsUiState::Success)
        .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000), // 5 seconds timeout for  performance reasons
        initialValue = ChatRoomsUiState.Loading
        )

    fun addChatRoom(title: String) {
        viewModelScope.launch {
            chatRepository.addChatRoom(title)
        }

    }



}