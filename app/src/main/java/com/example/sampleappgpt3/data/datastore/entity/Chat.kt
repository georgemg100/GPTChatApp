package com.example.sampleappgpt3.data.datastore.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["uid"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Chat(
                @PrimaryKey(autoGenerate = true) val chatId: Int = 0,
                @ColumnInfo(name = "chat_title") val title: String?,
                @ColumnInfo(name = "user_id") val userId: Int?
                )