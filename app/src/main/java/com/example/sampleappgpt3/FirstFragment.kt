package com.example.sampleappgpt3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.sampleappgpt3.data.datastore.entity.Chat
import com.example.sampleappgpt3.databinding.FragmentFirstBinding
import com.example.sampleappgpt3.ui.ChatRoomsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@AndroidEntryPoint
class FirstFragment : Fragment() {

    val viewModel: ChatRoomsViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var chatRoomsUiState : ChatRoomsUiState by mutableStateOf(ChatRoomsUiState.Loading)

        lifecycleScope.launch {
            viewLifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatRooms
                    .onEach {
                        chatRoomsUiState = it
                    }
                    .collect()
            }
        }
        return ComposeView(requireContext()).apply {
            setContent {
                // Call your composable function to build the UI
                when(chatRoomsUiState) {
                    ChatRoomsUiState.Loading -> {
                        Text("No existing chats")
                    }
                    is ChatRoomsUiState.Success -> {
                        topLevelUI((chatRoomsUiState as ChatRoomsUiState.Success).chatRooms)
                    }
                }
            }
        }
    }

    @Composable
    fun topLevelUI(chatRooms: List<Chat>) {
        Box(modifier = Modifier.fillMaxSize()) {
            CardItemList(chatRooms)
            FloatingActionButton(onClick = { viewModel.addChatRoom("room #" + chatRooms.size)}, modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp) ) {
                Icon(Icons.Filled.Create,  contentDescription = stringResource(id = R.string.action_settings))
            }
        }
    }

    @Composable
    fun CardItemView(cardItem: Chat) {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .clickable {
                    activity?.supportFragmentManager?.commit() {
                        //addToBackStack("")
                        add(R.id.fragment_container, SecondFragment())

                    }
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
    fun CardItemList(chatRooms: List<Chat>) {

        LazyColumn {
            items(chatRooms) { chatRoom ->
                CardItemView(chatRoom)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.buttonFirst.setOnClickListener {
//            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //_binding = null
    }
}