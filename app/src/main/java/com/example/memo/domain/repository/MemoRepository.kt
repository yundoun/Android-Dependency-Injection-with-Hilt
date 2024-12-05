package com.example.memo.domain.repository

import com.example.memo.domain.model.Memo
import kotlinx.coroutines.flow.Flow

interface MemoRepository {
    fun getMemos(): Flow<List<Memo>>
    suspend fun insertMemo(memo: Memo)
    suspend fun deleteMemo(memo: Memo)
}