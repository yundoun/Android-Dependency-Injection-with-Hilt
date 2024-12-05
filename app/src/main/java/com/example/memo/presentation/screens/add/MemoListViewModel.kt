package com.example.memo.presentation.screens.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memo.domain.model.Memo
import com.example.memo.domain.usecase.DeleteMemoUseCase
import com.example.memo.domain.usecase.GetMemosUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MemoListState(
    val memos: List<Memo> = emptyList(),
    val isLoading: Boolean = false
)

@HiltViewModel // HilViewModel 임을 명시
class MemoListViewModel @Inject constructor( // 생성자 주입
    private val getMemosUseCase: GetMemosUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase
) : ViewModel(){

    private val _state = MutableStateFlow(MemoListState())
    val state: StateFlow<MemoListState> = _state

    init {
        getMemos()
    }

    private fun getMemos() {
        _state.value = state.value.copy(isLoading = true)
        getMemosUseCase().onEach { memos ->
            _state.value = state.value.copy(
                memos = memos,
                isLoading = false
            )
        }.launchIn(viewModelScope)
    }

    fun deleteMemo(memo: Memo) {
        viewModelScope.launch {
            deleteMemoUseCase(memo)
        }
    }

}