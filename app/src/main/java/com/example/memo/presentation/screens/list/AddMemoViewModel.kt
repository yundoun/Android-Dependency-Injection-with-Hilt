package com.example.memo.presentation.screens.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.memo.domain.usecase.AddMemoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 의존성 체인:
// ViewModel은 UseCase를 주입받음
// Usecase는 Repository를 주입받음
// Repository는 DAO를 주입받음
// DAO는 Database를 주입받음


@HiltViewModel
class AddMemoViewModel @Inject constructor(
// 생성자 주입을 통해 UseCase들을 주입받는다. Hilt가 이 생성자를 사용해서 ViewModel 인스턴스를 생성한다.
    private val addMemoUseCase: AddMemoUseCase
) : ViewModel() {
    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: AddMemoEvent) {
        when (event) {
            is AddMemoEvent.SaveMemo -> {
                viewModelScope.launch {
                    try {
                        addMemoUseCase(
                            title = event.title,
                            content = event.content
                        )
                        _eventFlow.emit(UiEvent.SaveMemo)
                    } catch (e: Exception) {
                        _eventFlow.emit(UiEvent.ShowError("메모 저장 실패"))
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data object SaveMemo : UiEvent()
        data class ShowError(val message: String) : UiEvent()
    }
}



sealed class AddMemoEvent {
    data class SaveMemo(val title: String, val content: String) : AddMemoEvent()
}