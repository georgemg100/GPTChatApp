package com.example.sampleappgpt3.data.model

import com.google.gson.annotations.SerializedName

data class GPTModel(
    @SerializedName("choices")
    val choices: List<Choice>,
)