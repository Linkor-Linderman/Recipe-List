package com.example.recipe_list.presentation.recipeDetails

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_list.common.Constants
import com.example.recipe_list.common.Resource
import com.example.recipe_list.domain.model.RecipeDetails
import com.example.recipe_list.domain.useCase.RecipeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val useCases: RecipeUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage: MutableState<String?> = mutableStateOf(null)
    val errorMessage: State<String?> = _errorMessage

    private val _recipeDetails: MutableState<RecipeDetails?> = mutableStateOf(null)
    val recipeDetails: State<RecipeDetails?> = _recipeDetails

    init {
        fetchRecipeDetails(savedStateHandle)
    }

    private fun fetchRecipeDetails(
        savedStateHandle: SavedStateHandle
    ) {
        val id: String = checkNotNull(savedStateHandle[Constants.ID])
        useCases.getRecipeDetailsUseCase(id).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.message
                }
                is Resource.Loading -> {
                    _isLoading.value = true
                    _errorMessage.value = null
                }
                is Resource.Success -> {
                    _isLoading.value = false
                    _errorMessage.value = null
                    _recipeDetails.value = result.data
                }
            }
        }.launchIn(viewModelScope)
    }
}