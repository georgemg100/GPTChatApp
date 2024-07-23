package com.example.sampleappgpt3.data.datastore.entity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sampleappgpt3.data.datastore.dao.ChatDao
import com.example.sampleappgpt3.data.datastore.dao.MessageDao
import com.example.sampleappgpt3.data.datastore.dao.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [User::class, Message::class, Chat::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun messageDao(): MessageDao
    abstract fun chatDao(): ChatDao

}