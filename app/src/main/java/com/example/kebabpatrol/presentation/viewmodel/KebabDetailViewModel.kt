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

sealed interface KebabState {
    data object Loading : KebabState
    data class Success(val kebab: KebabPlace) : KebabState
    data class Error(val message: String) : KebabState
}

@HiltViewModel
class KebabDetailViewModel @Inject constructor(
    private val repository: KebabRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val kebabId: Int = checkNotNull(savedStateHandle["kebabId"])

    private val _state = MutableStateFlow<KebabState>(KebabState.Loading)
    val state = _state.asStateFlow()

    init {
        loadKebab()
    }

    fun loadKebab() {
        viewModelScope.launch {
            _state.value = KebabState.Loading
            try {
                val kebab = repository.getKebabById(kebabId)

                if (kebab != null) {
                    _state.value = KebabState.Success(kebab)
                } else {
                    _state.value = KebabState.Error("Target lost. Kebab ID $kebabId not found.")
                }
            } catch (e: Exception) {
                _state.value = KebabState.Error("Intel failure: ${e.message}")
            }
        }
    }
}