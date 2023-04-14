package com.example.sampleappgpt3.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleappgpt3.ChatRoomsUiState
import com.example.sampleappgpt3.MessagesUiState
import com.example.sampleappgpt3.data.ChatRepository
import com.example.sampleappgpt3.data.datastore.entity.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(val chatRepository: ChatRepository): ViewModel() {

    //var chatMessages: StateFlow<MessagesUiState>? = null

    var cachedMessagesStateFlow: StateFlow<MessagesUiState>? =  null
    var cachedMessagesStateFlow2: StateFlow<List<Message>>? =  null

    var chatId: Int? = null;
//    fun getChatMessages(chatId: Int) {
//        chatMessages = chatRepository.getMessages(chatId)
//            .map(MessagesUiState::Success)
//            .stateIn(scope = viewModelScope,
//                started = SharingStarted.WhileSubscribed(5_000),
//                MessagesUiState.Loading)
//    }

     fun chatMessages(chatId: Int): StateFlow<MessagesUiState> {
         return cachedMessagesStateFlow?.let {
             return cachedMessagesStateFlow as StateFlow<MessagesUiState>
         } ?: run {
             cachedMessagesStateFlow =
                 chatRepository.getMessages(chatId)
                     .map(MessagesUiState::Success)
                     .stateIn(scope = viewModelScope,
                         started = SharingStarted.WhileSubscribed(5_000),
                         MessagesUiState.Loading)
             cachedMessagesStateFlow as StateFlow<MessagesUiState>
         }
     }

    fun chatMessages3(chatId: Int): StateFlow<List<Message>> {
        return cachedMessagesStateFlow2?.let {
            return cachedMessagesStateFlow as StateFlow<List<Message>>
        } ?: run {
            cachedMessagesStateFlow2 =
                chatRepository.getMessages(chatId)
                    //.map(MessagesUiState::Success)
                    .stateIn(scope = viewModelScope,
                        started = SharingStarted.WhileSubscribed(5_000),
                        emptyList<Message>()
                    )
            cachedMessagesStateFlow as StateFlow<List<Message>>
        }
    }


    val chatMessages: StateFlow<MessagesUiState> = chatRepository.getMessages(chatId)
        .map(MessagesUiState::Success)
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            MessagesUiState.Loading)

    fun chatMessages2(chatId: Int) {
        viewModelScope.launch {
            chatRepository.getMessages(chatId).collect() {
                it.forEach() {
                    println(it)
                }
            }
        }
    }

    fun allMessages(): StateFlow<MessagesUiState> = chatRepository.getAllMessages()

        .map(MessagesUiState::Success)
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            MessagesUiState.Loading)

    fun allMessages2(): StateFlow<MessagesUiState> {
        return cachedMessagesStateFlow?.let {
            return cachedMessagesStateFlow as StateFlow<MessagesUiState>
        } ?: run {
            cachedMessagesStateFlow =
            chatRepository.getAllMessages()
                .map(MessagesUiState::Success)
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5_000),
                    MessagesUiState.Loading
                )
             cachedMessagesStateFlow as StateFlow<MessagesUiState>
        }
    }

     fun insertMessage(message: String, chatId: Int) {
         viewModelScope.launch {
             chatRepository.insertMessage(message, chatId)
         }
     }

}