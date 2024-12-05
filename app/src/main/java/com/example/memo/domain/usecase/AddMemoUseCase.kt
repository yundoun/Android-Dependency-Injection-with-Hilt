package com.example.memo.domain.usecase

import com.example.memo.domain.model.Memo
import com.example.memo.domain.repository.MemoRepository
import javax.inject.Inject

class AddMemoUseCase @Inject constructor(
    private val repository: MemoRepository
) {
    suspend operator fun invoke(title: String, content: String) {
        val memo = Memo(
            title = title.trim(),
            content = content.trim()
        )
        repository.insertMemo(memo)
    }
}