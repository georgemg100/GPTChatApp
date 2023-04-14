package com.example.sampleappgpt3.di

import com.example.sampleappgpt3.data.datastore.dao.ChatDao
import com.example.sampleappgpt3.data.datastore.dao.MessageDao
import com.example.sampleappgpt3.data.datastore.dao.UserDao
import com.example.sampleappgpt3.data.datastore.entity.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesMessageDao(
        database: AppDatabase,
    ): MessageDao = database.messageDao()

    @Provides
    fun providesNewsResourceDao(
        database: AppDatabase,
    ): UserDao = database.userDao()

    @Provides
    fun providesChatDao(
        database: AppDatabase,
    ): ChatDao = database.chatDao()
}
