package com.example.sampleappgpt3.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.sampleappgpt3.MessagesUiState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun MessagesRoute(messageViewModel: MessageViewModel = hiltViewModel(), chatId: Int) {
    val messagesUiState: MessagesUiState by messageViewModel.chatMessages(chatId).collectAsStateWithLifecycle()
    val isLoading: Boolean by messageViewModel.isLoadingStateFlow.collectAsStateWithLifecycle()

    //messagesUiState =
    when (val state = messagesUiState) { // allows for smart casting
        MessagesUiState.Loading -> Unit
        is MessagesUiState.Success -> {
//            if(state.chatMessages.isNotEmpty() && state.chatMessages.last().userID == 2) {
//                isLoading.value = false
//            }
            topLevelUI(viewModel = messageViewModel, chatId = chatId, messagesUiState = state, isLoading)
        }
    }
}

@Composable
fun topLevelUI(viewModel: MessageViewModel, chatId: Int, messagesUiState: MessagesUiState.Success, isLoading: Boolean) {

    Column(modifier = Modifier
        .fillMaxSize()
        .wrapContentHeight()) {
        val lazyListState = rememberLazyListState()
        // When the list changes, scroll to the last index
        LaunchedEffect(key1 = messagesUiState.chatMessages) {
            if (messagesUiState.chatMessages.isNotEmpty()) {
                lazyListState.animateScrollToItem(messagesUiState.chatMessages.size - 1)
            }
        }

        LazyColumn(state = lazyListState, modifier = Modifier
            .fillMaxWidth()
            .weight(1f)) {
            items(messagesUiState.chatMessages) { message ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {

                    val modifier =
                        if (message.userID == 2) Modifier
                            .align(Alignment.TopStart)
                            .fillMaxWidth()
                            .background(Color(0, 0, 255)) else Modifier
                            .align(
                                Alignment.TopEnd
                            )
                            .fillMaxWidth()
                            .background(Color(255, 0, 0))
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
            if (isLoading) {
                item {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
            }
        }

        MyTextInputWithSendButton(
            modifier = Modifier.wrapContentHeight(),
            viewModel,
            chatId,
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
                imeAction = ImeAction.Send,
            ),
            keyboardActions = KeyboardActions(onSend = {
                viewModel.insertMessage(text.value, chatId)
                text.value = ""
            }),
            textStyle = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f),
        )

        Button(
            onClick = {
                        viewModel.insertMessage(text.value, chatId)
                        text.value = ""
                      },
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
