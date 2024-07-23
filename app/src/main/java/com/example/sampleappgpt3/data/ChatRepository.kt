package com.example.sampleappgpt3.data

import com.example.sampleappgpt3.data.datasource.GPTDatasource
import com.example.sampleappgpt3.data.datastore.dao.ChatDao
import com.example.sampleappgpt3.data.datastore.dao.MessageDao
import com.example.sampleappgpt3.data.datastore.dao.UserDao
import com.example.sampleappgpt3.data.datastore.entity.Chat
import com.example.sampleappgpt3.data.datastore.entity.Message
import com.example.sampleappgpt3.data.datastore.entity.User
import com.example.sampleappgpt3.data.model.CompletionPrompt
import com.example.sampleappgpt3.data.model.GPTModel
import com.example.sampleappgpt3.data.model.MessageRoleAndContent
import com.example.sampleappgpt3.data.model.streaming.StreamResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response
import retrofit2.await
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import javax.inject.Inject


class ChatRepository @Inject constructor(private val messageDao: MessageDao,
                                         private val chatDao: ChatDao,
                                         private val userDao: UserDao,
                                         private val gptDatasource: GPTDatasource) {

    fun getChats() : Flow<List<Chat>> = chatDao.getAll()

    suspend fun addChatRoom(title: String?) {
        withContext(Dispatchers.IO) {
            chatDao.insert(Chat(0, title, 1))
        }
    }

    suspend fun insertUsers() {
        withContext(Dispatchers.IO) {
            userDao.insertAll(User(1, "Michael", "G"))
            //userDao.insertAll(User(0, "GPT", ""))
        }
    }

    suspend fun insertGPTUser() {
        withContext(Dispatchers.IO) {
            userDao.insertAll(User(0, "GPT", ""))
        }
    }

    suspend fun doesUserExist(id: Int): Boolean {
        var user: User? = null
        withContext(Dispatchers.IO) {
            user = userDao.getUserById(id)
        }
        return user != null
    }

    fun getMessages(chatId: Int): Flow<List<Message>> {
        //messageDao.getMessagesForChat(chatId!!)
        return messageDao.getMessagesForChat(chatId)
    }

    suspend fun insertMessage(message: String, chatId: Int) {
        withContext(Dispatchers.IO) {
            messageDao.insertMessage(Message(0, 1, message, getCurrentDateAsString(), chatId))
        }
    }

    suspend fun sendMessageToGPT(message: String): GPTModel {
        try {
            return gptDatasource.getCompletion(
                "application/json",
                CompletionPrompt("gpt-3.5-turbo", listOf(MessageRoleAndContent("user", message)))
            ).await()
        } catch(e: HttpException) {
            if (e.code() == 401) {
                // Handle 401 error, maybe log the user out or show an error message
                throw Exception("Unauthorized: Please check your credentials.")
            } else {
                // Handle other HTTP errors
                throw e
            }
        }
    }

    suspend fun queryGPTAndSaveResult(message: String, chatId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            val gptModel = sendMessageToGPT(message)
            messageDao.insertMessage(Message(0, 2, gptModel.choices[0].message.content, getCurrentDateAsString(), chatId))
            //MutableStateFlow(true)
            true
        }
    }

    fun getCurrentDateAsString(): String {
        val currentDate = Calendar.getInstance()
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return formatter.format(currentDate.time)
    }

}