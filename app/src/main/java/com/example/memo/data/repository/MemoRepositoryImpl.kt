package com.example.memo.data.repository

import com.example.memo.data.local.MemoDao
import com.example.memo.data.local.toEntity
import com.example.memo.domain.model.Memo
import com.example.memo.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoRepositoryImpl @Inject constructor(
    private val dao: MemoDao
): MemoRepository{
    override fun getMemos(): Flow<List<Memo>> {
        return dao.getMemos().map { entities ->
            entities.map { entity ->
                entity.toMemo()
            }
        }
    }

    override suspend fun insertMemo(memo: Memo) {
        dao.insertMemo(memo.toEntity())
    }

    override suspend fun deleteMemo(memo: Memo) {
        dao.deleteMemo(memo.toEntity())
    }
}