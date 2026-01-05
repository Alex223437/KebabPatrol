package com.example.kebabpatrol.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KebabAddViewModel @Inject constructor(
    private val repository: KebabRepository
) : ViewModel() {

    val name = mutableStateOf("")
    val description = mutableStateOf("")
    val rating = mutableStateOf("")
    val isLoading = mutableStateOf(false)

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onNameChange(newValue: String) { name.value = newValue }
    fun onDescriptionChange(newValue: String) { description.value = newValue }
    fun onRatingChange(newValue: String) { rating.value = newValue }

    fun saveKebab(lat: Double, lng: Double) {
        viewModelScope.launch {
            if (name.value.isBlank() || description.value.isBlank()) {
                _uiEvent.send(UiEvent.ShowSnackbar("Fill in all fields, bro!"))
                return@launch
            }

            val ratingDouble = rating.value.toDoubleOrNull()
            if (ratingDouble == null || ratingDouble < 1.0 || ratingDouble > 5.0) {
                _uiEvent.send(UiEvent.ShowSnackbar("Rating must be between 1.0 and 5.0!"))
                return@launch
            }

            isLoading.value = true
            try {
                repository.insertKebab(
                    KebabPlace(
                        id = 0,
                        name = name.value,
                        description = description.value,
                        rating = ratingDouble,
                        lat = lat,
                        lng = lng,
                        image = "https://www.savorythoughts.com/wp-content/uploads/2021/09/Doner-Kebab-Recipe-Savory-Thoughts-8.jpg"
                    )
                )
                _uiEvent.send(UiEvent.Success)
            } catch (e: Exception) {
                _uiEvent.send(UiEvent.ShowSnackbar("Error: ${e.message}"))
            } finally {
                isLoading.value = false
            }
        }
    }

    sealed class UiEvent {
        object Success : UiEvent()
        data class ShowSnackbar(val message: String) : UiEvent()
    }
}