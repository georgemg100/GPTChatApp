package com.example.sampleappgpt3.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.R
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.commit
import com.example.sampleappgpt3.SecondFragment
import com.example.sampleappgpt3.data.datastore.entity.Chat

@Composable
fun ChatsScreen(chatRooms: List<Chat>,
                onFABClicked: () -> Unit,
                onClickChatRoom: (Int) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        CardItemList(chatRooms, onClickChatRoom)
        FloatingActionButton(onClick = { onFABClicked()}, modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(12.dp) ) {
            Icon(Icons.Filled.Create,  contentDescription = "create new chat")
        }
    }
}

@Composable
fun CardItemView(cardItem: Chat, onClickChatRoom: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clickable {
                onClickChatRoom(cardItem.chatId)
            },
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp),

    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(text = cardItem.title!!, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "chat room", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun CardItemList(chatRooms: List<Chat>, onClickChatRoom: (Int) -> Unit) {
    LazyColumn {
        items(chatRooms) { chatRoom ->
            CardItemView(chatRoom, onClickChatRoom)
        }
    }

}
