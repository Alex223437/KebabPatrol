package com.example.kebabpatrol.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KebabListViewModel @Inject constructor(
    private val repository: KebabRepository // <--- ВНЕДРЯЕМ ДИЛЕРА (REPO)
) : ViewModel() {

    // Состояние списка: сперва пусто
    private val _kebabList = MutableStateFlow<List<KebabPlace>>(emptyList())
    val kebabList: StateFlow<List<KebabPlace>> = _kebabList.asStateFlow()

    // Состояние загрузки (крутилка)
    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    // Состояние ошибки (если инет отвалился)
    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        loadKebabs()
    }

    fun loadKebabs() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                // ИДЕМ НА ДЕЛО (В СЕТЬ)
                val result = repository.getKebabs()
                _kebabList.value = result
                Log.d("KEBAB_TAG", "Загружено: ${result.size} точек")
            } catch (e: Exception) {
                // ЕСЛИ МУСОРА ПОВЯЗАЛИ (ОШИБКА)
                Log.e("KEBAB_TAG", "Ошибка: ${e.message}")
                _error.value = "Брат, беда: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}