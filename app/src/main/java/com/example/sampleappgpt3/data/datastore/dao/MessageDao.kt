package com.example.sampleappgpt3.data.datastore.dao

import androidx.room.*
import com.example.sampleappgpt3.data.datastore.entity.Message
import com.example.sampleappgpt3.data.datastore.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM message")
    fun getAll(): Flow<List<Message>>

    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
    fun loadAllByIds(userIds: IntArray): List<User>

    @Query("SELECT * FROM user WHERE first_name LIKE :first AND " +
            "last_name LIKE :last LIMIT 1")
    fun findByName(first: String, last: String): User

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT * FROM message WHERE chat_Id = :chatId")
    fun getMessagesForChat(chatId: Int) : Flow<List<Message>>

    @Insert
    fun insertMessage(message: Message)

    @Upsert
    fun upsertMessage(message: Message)
}