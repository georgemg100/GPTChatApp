package com.example.sampleappgpt3.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.sampleappgpt3.data.ChatRepository
import com.example.sampleappgpt3.data.datastore.dao.UserDao
import com.example.sampleappgpt3.data.datastore.entity.AppDatabase
import com.example.sampleappgpt3.data.datastore.entity.User
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesNiaDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "gpt-chat-database")
            .fallbackToDestructiveMigration()
            .build()
    }
}