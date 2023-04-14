package com.example.sampleappgpt3

import android.app.Application
import com.example.sampleappgpt3.data.ChatRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class ChatApplication : Application() {

    @Inject
    lateinit var chatRepository: ChatRepository
    private val applicationJob = SupervisorJob()
    private val applicationScope = CoroutineScope(Dispatchers.Main + applicationJob)

    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            if(!chatRepository.doesUserExist(1)) {
                chatRepository.insertUser()
            }
        }

    }

    override fun onTerminate() {
        super.onTerminate()
        applicationJob.cancel()
    }

}