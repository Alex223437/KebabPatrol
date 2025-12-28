package com.example.kebabpatrol.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. СОСТОЯНИЕ (State) - ЧТО ПРОИСХОДИТ НА ЭКРАНЕ
// (Можешь вынести в отдельный файл KebabState.kt, но можно и тут оставить, чтоб не потерял)
sealed interface KebabState {
    data object Loading : KebabState // Крутим барабан
    data class Success(val kebab: KebabPlace) : KebabState // Мясо найдено!
    data class Error(val message: String) : KebabState // Менты повязали (ошибка)
}

@HiltViewModel
class KebabDetailViewModel @Inject constructor(
    private val repository: KebabRepository, // Наш склад с данными
    savedStateHandle: SavedStateHandle // Сюда падает ID из навигации (малява)
) : ViewModel() {

    // Вытаскиваем ID сразу. "kebabId" должно совпадать с тем, что мы писали в MainActivity (route)
    private val kebabId: Int = checkNotNull(savedStateHandle["kebabId"])

    // Состояние экрана (по умолчанию - загрузка)
    private val _state = MutableStateFlow<KebabState>(KebabState.Loading)
    val state = _state.asStateFlow()

    init {
        // Как только VM создалась - сразу ищем кебаб
        loadKebab()
    }

    fun loadKebab() {
        viewModelScope.launch {
            _state.value = KebabState.Loading
            try {
                // ТУТ ДОЛЖНА БЫТЬ ФУНКЦИЯ В ТВОЕМ РЕПОЗИТОРИИ!
                // Если её нет - добавь в KebabRepository: suspend fun getKebabById(id: Int): KebabPlace?
                val kebab = repository.getKebabById(kebabId)

                if (kebab != null) {
                    _state.value = KebabState.Success(kebab)
                } else {
                    _state.value = KebabState.Error("Шаурма не найдена, начальник! Сбежала!")
                }
            } catch (e: Exception) {
                _state.value = KebabState.Error("Ошибка связи с общаком: ${e.message}")
            }
        }
    }
}