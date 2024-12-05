package com.example.memo.domain.usecase

import com.example.memo.domain.model.Memo
import com.example.memo.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMemosUseCase @Inject constructor(
    private val repository: MemoRepository
) {
    operator fun invoke(): Flow<List<Memo>> {
        return repository.getMemos()
    }
}

