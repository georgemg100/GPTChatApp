package com.example.sampleappgpt3.ui

import android.annotation.SuppressLint
import android.widget.EditText
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sampleappgpt3.MessagesUiState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.semantics.Role.Companion.Button
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sampleappgpt3.data.datastore.entity.Message
import kotlinx.coroutines.flow.*


@Composable
fun MessagesRoute(messageViewModel: MessageViewModel = hiltViewModel(), chatId: Int) {
    println("MessagesRoute")
    //val viewModel: MessageViewModel = viewModel()
    //messageViewModel.getChatMessages(chatId)
    //messageViewModel.chatId = chatId
//    var messagesUiState: MessagesUiState by remember { mutableStateOf(MessagesUiState.Loading) }
//    var messagesUiStateCopy: MessagesUiState? = null
//    messageViewModel.chatMessages(chatId).onEach {
//        messagesUiStateCopy = it
//    }
//    messagesUiStateCopy?.let {
//        messagesUiState = it
//    } ?: {}
    val messagesUiState: MessagesUiState by messageViewModel.chatMessages(chatId).collectAsStateWithLifecycle()
    //val messagesUiState: List<Message> by messageViewModel.chatMessages3(chatId).collectAsStateWithLifecycle()

    //messagesUiState =
    when (val state = messagesUiState) { // allows for smart casting
        MessagesUiState.Loading -> Unit
        is MessagesUiState.Success -> {
            //val chatMessagesState: MutableList<Message> = remember { mutableStateListOf<Message>((messagesUiState as MessagesUiState.Success).chatMessages) }
            (messagesUiState as MessagesUiState.Success).chatMessages
            //chatMessagesState.addAll((messagesUiState as MessagesUiState.Success).chatMessages)

            topLevelUI(viewModel = messageViewModel, chatId = chatId, messagesUiState = state, chatMessagesState = chatMessagesState)
        }
    }
}


@Composable
fun topLevelUI(viewModel: MessageViewModel, chatId: Int, messagesUiState: MessagesUiState.Success, chatMessagesState: List<Message>) {
//    val filteredMessages = messagesUiState.chatMessages.filter {
//        it.chatId == chatId
//    }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(chatMessagesState) { message ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    val modifier =
                        if (message.userID == 0) Modifier.align(Alignment.TopStart) else Modifier.align(
                            Alignment.TopEnd
                        )
                    //MessageItem(message.text, modifier)
                    Card(
                        modifier = modifier
                            .padding(4.dp)
                            .width(100.dp),
                        elevation = 10.dp
                    ) {
                        Text(
                            text = message.text,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier
                                .wrapContentWidth()
                                .padding(12.dp)
                        )
                    }
                }
            }
        }
        MyTextInputWithSendButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            viewModel,
            chatId
        )
    }
}

@Composable
fun MyTextInputWithSendButton(modifier: Modifier, viewModel: MessageViewModel, chatId: Int) {
    val text = remember { mutableStateOf("") }

    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        TextField(
            value = text.value,
            onValueChange = { newValue -> text.value = newValue },
            label = { Text("Enter text") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(onSend = { viewModel.insertMessage(text.value, chatId) }),
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )

        Button(
            onClick = { viewModel.insertMessage(text.value, chatId) },
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(Icons.Filled.Send, contentDescription = "Send")
        }
    }
}

@Composable
fun MessageItem(text: String, modifier: Modifier) {
    Card(modifier = modifier.padding(4.dp),
        elevation = 10.dp) {
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}
