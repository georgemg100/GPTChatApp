package com.example.sampleappgpt3.data.datastore.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sampleappgpt3.data.datastore.dao.ChatDao
import com.example.sampleappgpt3.data.datastore.dao.MessageDao
import com.example.sampleappgpt3.data.datastore.dao.UserDao

@Database(entities = [User::class, Message::class, Chat::class], version = 3)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao
}