package com.example.mycity.ui

import androidx.lifecycle.ViewModel
import com.example.mycity.data.PlacesRepository
import com.example.mycity.model.Category
import com.example.mycity.model.Place
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class MyCityUiState(
    val selectedCategory: Category = Category.RESTAURANTS,
    val places: List<Place> = PlacesRepository.getPlacesByCategory(Category.RESTAURANTS),
    val selectedPlace: Place? = null
)

class MyCityViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MyCityUiState())
    val uiState: StateFlow<MyCityUiState> = _uiState.asStateFlow()

    fun selectCategory(category: Category) {
        _uiState.update {
            it.copy(
                selectedCategory = category,
                places = PlacesRepository.getPlacesByCategory(category),
                selectedPlace = null
            )
        }
    }

    fun selectPlace(place: Place) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun clearSelection() {
        _uiState.update { it.copy(selectedPlace = null) }
    }
}