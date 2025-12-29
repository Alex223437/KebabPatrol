package com.example.kebabpatrol.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kebabpatrol.domain.model.KebabPlace
import com.example.kebabpatrol.domain.repository.KebabRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class KebabListViewModel @Inject constructor(
    private val repository: KebabRepository
) : ViewModel() {

    private val _kebabList = MutableStateFlow<List<KebabPlace>>(emptyList())
    val kebabList = _kebabList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    init {
        observeKebabs()
    }

    private fun observeKebabs() {
        viewModelScope.launch {
            _isLoading.value = true

            // WE DON'T JUST "GET" IT. WE "COLLECT" IT.
            repository.getKebabs()
                .catch { e ->
                    // Handle stream errors (Database crashes)
                    Log.e("KEBAB_TAG", "Error observing stream: ${e.message}")
                    _error.value = "Data corruption: ${e.message}"
                    _isLoading.value = false
                }
                .collect { list ->
                    // Every time the DB updates, this block runs automatically
                    _kebabList.value = list
                    _isLoading.value = false
                    Log.d("KEBAB_TAG", "Stream updated: ${list.size} targets found")
                }
        }
    }
}