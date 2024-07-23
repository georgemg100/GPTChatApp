package com.example.sampleappgpt3

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Spinner
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import com.example.sampleappgpt3.data.datastore.entity.Chat
import com.example.sampleappgpt3.databinding.ActivityMainBinding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.commit
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sampleappgpt3.data.datastore.entity.Message
import com.example.sampleappgpt3.ui.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

sealed interface ChatRoomsUiState {

    object Loading : ChatRoomsUiState
    data class Success(
        val chatRooms: List<Chat>,
    ) : ChatRoomsUiState
}

sealed interface MessagesUiState {

    object Loading : MessagesUiState
    data class Success(
        val chatMessages: List<Message>,
    ) : MessagesUiState
}


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    val viewModel: ChatRoomsViewModel by viewModels()
    val messageViewModel: MessageViewModel by viewModels()

    @Composable
    private fun AppBar() {
        androidx.compose.material.TopAppBar(
            navigationIcon = {
                androidx.compose.material.Icon(
                    imageVector = Icons.Rounded.Menu,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            },
            title = {
                androidx.compose.material.Text(text = "GPT sample app")
            },
            backgroundColor = MaterialTheme.colors.primary
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var chatRoomsUiState: ChatRoomsUiState by mutableStateOf(ChatRoomsUiState.Loading)
//        lifecycleScope.launch {
//            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.chatRooms
//                    .onEach {
//                        chatRoomsUiState = it
//                    }
//                    .collect()
//            }
//        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.chatRooms.collect {
                    chatRoomsUiState = it
                }
            }
        }
        //mutableListOf<Int>(1,2,3,4,5,6).map { it * it }

        setContent {

            MaterialTheme {
                androidx.compose.material.Scaffold(
                    topBar = { AppBar() }
                ) { innerPadding ->

                    val navController = rememberNavController()
                    println("isSystemInDarkTheme" + isSystemInDarkTheme())
                    NavHost(
                        navController = navController,
                        startDestination = ChatRooms.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = ChatRooms.route) {
                            when (chatRoomsUiState) {
                                ChatRoomsUiState.Loading -> {
                                    Text("Loading")
                                }
                                is ChatRoomsUiState.Success -> {
                                    ChatsScreen((chatRoomsUiState as ChatRoomsUiState.Success).chatRooms,
                                        onFABClicked = {
                                            viewModel.addChatRoom("room #" + (chatRoomsUiState as ChatRoomsUiState.Success).chatRooms.size)
                                        },
                                        onClickChatRoom = { chatId ->
                                            print("navigate: " + "${Messages.route}/$chatId")

                                            navController.navigate("${Messages.route}/$chatId") {
                                                launchSingleTop = true
                                            }
                                        })
                                }
                            }
                        }
                        composable(route = Messages.routeWithArgs, arguments = Messages.arguments) {
                            print("it.arguments: " + it.arguments!!)
                            val chatRoomId =
                                it.arguments?.getInt(Messages.chatIdArg)
                            MessagesRoute(chatId = chatRoomId!!)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val listPerson = mutableListOf<Person>(Female(), Male())
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}

open class Person {
    val lazyVal: String by lazy {
        println("computed")
        "computed"
    }

    val observableVal: String by Delegates.observable("initial") { prop, old, new ->
        println("$old -> $new")
    }
}

class Female : Person() {

}

class Male : Person() {

}
