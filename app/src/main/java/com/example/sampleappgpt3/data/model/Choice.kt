package com.example.sampleappgpt3.data.model

import com.google.gson.annotations.SerializedName

data class Choice(@SerializedName("message") val message : MessageRoleAndContent,
                  @SerializedName("finish_reason") val finishReason: String,
                  @SerializedName("index") val index: Int
                  )
