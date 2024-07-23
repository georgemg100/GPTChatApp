package com.example.sampleappgpt3.data.model
import com.google.gson.annotations.SerializedName

data class CompletionPrompt(
    @SerializedName("model") val modelval : String,
    @SerializedName("messages") val prompt: List<MessageRoleAndContent>,
    @SerializedName("temperature") val temperature: Double = .7,
    )
