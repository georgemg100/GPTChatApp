package com.example.sampleappgpt3.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sampleappgpt3.ChatRoomsUiState
import com.example.sampleappgpt3.MessagesUiState
import com.example.sampleappgpt3.data.ChatRepository
import com.example.sampleappgpt3.data.datastore.entity.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(val chatRepository: ChatRepository): ViewModel() {

    //var chatMessages: StateFlow<MessagesUiState>? = null
    var cachedMessagesStateFlow: StateFlow<MessagesUiState>? =  null
    var cachedMessagesStateFlow2: StateFlow<List<Message>>? =  null
    private var _isLoadingStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLoadingStateFlow: StateFlow<Boolean> = _isLoadingStateFlow

    var chatId: Int? = null;

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

     fun insertMessage(message: String, chatId: Int) {
         viewModelScope.launch {
             try {
                 chatRepository.insertMessage(message, chatId)
                 _isLoadingStateFlow.value = true
                 _isLoadingStateFlow.value = !chatRepository.queryGPTAndSaveResult(message, chatId)
             } catch(e: Exception) {
                 chatRepository.insertMessage("There was a problem sending this message, please check openai api key is added to project", chatId)
                 _isLoadingStateFlow.value = false
             }
         }
     }
}