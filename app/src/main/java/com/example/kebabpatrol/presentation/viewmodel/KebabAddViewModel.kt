package com.example.kebabpatrol.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kebabpatrol.domain.usecase.AddKebabUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KebabAddViewModel @Inject constructor(
    private val addKebabUseCase: AddKebabUseCase
) : ViewModel() {

    // Состояние полей (чтоб буквы на экране появлялись)
    private val _name = mutableStateOf("")
    val name: State<String> = _name

    private val _description = mutableStateOf("")
    val description: State<String> = _description

    private val _rating = mutableStateOf("") // Вводим как строку, потом перегоним в Double
    val rating: State<String> = _rating

    // Состояние загрузки (крутится - лавеха мутится)
    private val _isLoading = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    // События ввода
    fun onNameChange(value: String) { _name.value = value }
    fun onDescriptionChange(value: String) { _description.value = value }
    fun onRatingChange(value: String) { _rating.value = value }

    // ГЛАВНОЕ: КНОПКА "СОХРАНИТЬ"
    fun saveKebab(lat: Double, lng: Double, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val ratingDouble = _rating.value.toDoubleOrNull() ?: 1.0

                addKebabUseCase(
                    name = _name.value,
                    description = _description.value,
                    rating = ratingDouble,
                    lat = lat,
                    lng = lng
                )
                // Если не упали - зовем колбэк (например, закрыть экран)
                onSuccess()
            } catch (e: Exception) {
                // Тут можно ошибку показать, но нам похуй пока
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}