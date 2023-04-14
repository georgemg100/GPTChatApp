package com.example.sampleappgpt3.ui

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.navArgument

interface ChatDestination {
    val route: String
}

object ChatRooms: ChatDestination {
    override val route: String = "chat_rooms"
}

object Messages: ChatDestination {
    override val route: String = "messages"
    const val chatIdArg = "chatId"
    val routeWithArgs = "${route}/{${chatIdArg}}"
    val arguments = listOf(
        navArgument(chatIdArg) { type = NavType.IntType }
    )

}