package com.example.sampleappgpt3.data.datastore.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sampleappgpt3.data.datastore.entity.Chat
import com.example.sampleappgpt3.data.datastore.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {
    @Query("SELECT * FROM chat")
    fun getAll(): Flow<List<Chat>>

    @Insert
    suspend fun insert(chat: Chat)

}