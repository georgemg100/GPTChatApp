package com.example.sampleappgpt3.data.model.streaming

import com.google.gson.annotations.SerializedName

data class StreamResponse(
    @SerializedName("id") val id: String,
    @SerializedName("object") val objectName: String,
    @SerializedName("created") val created: Long,
    @SerializedName("model") val model: String,
    @SerializedName("choices") val choices: List<ChoiceStream>
)

data class ChoiceStream(
    @SerializedName("delta") val delta: DeltaStream,
    @SerializedName("index") val index: Int,
    @SerializedName("finish_reason") val finishReason: String?
)

data class DeltaStream(
    @SerializedName("content") val content: String
)