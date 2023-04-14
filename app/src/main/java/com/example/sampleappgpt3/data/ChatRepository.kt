package com.example.sampleappgpt3.data

import com.example.sampleappgpt3.data.datastore.dao.ChatDao
import com.example.sampleappgpt3.data.datastore.dao.MessageDao
import com.example.sampleappgpt3.data.datastore.dao.UserDao
import com.example.sampleappgpt3.data.datastore.entity.Chat
import com.example.sampleappgpt3.data.datastore.entity.Message
import com.example.sampleappgpt3.data.datastore.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class ChatRepository @Inject constructor(private val messageDao: MessageDao, private val  chatDao: ChatDao, private val userDao: UserDao) {

    fun getChats() : Flow<List<Chat>> = chatDao.getAll()

    suspend fun addChatRoom(title: String?) {
        withContext(Dispatchers.IO) {
            chatDao.insert(Chat(0, title, 1))
        }
    }

    suspend fun insertUser() {
        withContext(Dispatchers.IO) {
            userDao.insertAll(User(1, "Michael", "G"))
        }
    }

    suspend fun doesUserExist(id: Int): Boolean {
        var user: User? = null
        withContext(Dispatchers.IO) {
            user = userDao.getUserById(id)
        }
        return user != null
    }

    fun getMessages(chatId: Int?): Flow<List<Message>> {
        //messageDao.getMessagesForChat(chatId!!)
        chatId?.let { return messageDao.getMessagesForChat(it).distinctUntilChanged() { old, new ->
            return@distinctUntilChanged old != new
        } } ?: return emptyFlow()
    }

    suspend fun insertMessage(message: String, chatId: Int) {
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(Message(0, 1, message, getCurrentDateAsString(), chatId))
        }
    }

    fun getCurrentDateAsString(): String {
        val currentDate = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(currentDate.time)
    }

    fun getAllMessages(): Flow<List<Message>> {
        return messageDao.getAll()
    }

}