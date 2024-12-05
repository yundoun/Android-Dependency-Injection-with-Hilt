package com.example.memo.domain.usecase

import com.example.memo.domain.model.Memo
import com.example.memo.domain.repository.MemoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteMemoUseCase @Inject constructor(
    private val repository: MemoRepository
) {
    suspend operator fun invoke(memo: Memo) {
        repository.deleteMemo(memo)
    }
}