package com.example.memo.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.memo.domain.model.Memo

@Entity(tableName = "memo_table")
data class MemoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "content")
    val content: String,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
){
    fun toMemo(): Memo {
        return Memo(
            id = id,
            title = title,
            content = content,
            createdAt = createdAt
        )
    }
}

fun Memo.toEntity(): MemoEntity {
    return MemoEntity(
        id = id,
        title = title,
        content = content,
        createdAt = createdAt
    )
}