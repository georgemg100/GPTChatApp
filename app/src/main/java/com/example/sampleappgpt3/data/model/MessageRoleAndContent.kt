package com.example.sampleappgpt3.data.model

import com.google.gson.annotations.SerializedName

data class MessageRoleAndContent(@SerializedName("role") val role: String,
                                @SerializedName("content") val content: String)
