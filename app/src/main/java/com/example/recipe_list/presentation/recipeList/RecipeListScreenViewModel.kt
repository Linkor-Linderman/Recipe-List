package com.example.recipe_list.presentation.recipeList

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_list.common.Resource
import com.example.recipe_list.domain.model.RecipeInfo
import com.example.recipe_list.domain.useCase.RecipeUseCases
import com.example.recipe_list.domain.utill.OrderType
import com.example.recipe_list.domain.utill.RecipeOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class RecipeListScreenViewModel @Inject constructor(
    private val useCases: RecipeUseCases
) : ViewModel() {

    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()

    private val _isLoading: MutableState<Boolean> = mutableStateOf(false)
    val isLoading: State<Boolean> = _isLoading

    private val _errorMessage: MutableState<String?> = mutableStateOf(null)
    val errorMessage: State<String?> = _errorMessage

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _isVisible: MutableState<Boolean> = mutableStateOf(false)
    val isVisible: State<Boolean> = _isVisible

    private val _recipeOrder: MutableState<RecipeOrder> =
        mutableStateOf(RecipeOrder.Name(orderType = OrderType.Ascending))
    val recipeOrder: State<RecipeOrder> = _recipeOrder

    private val _recipes = MutableStateFlow(emptyList<RecipeInfo>())
    val recipes = searchText
        .debounce(100L)
        .onEach { _isSearching.update { true } }
        .combine(_recipes) { text, recipes ->
            if (text.isBlank()) {
                recipes
            } else {
                delay(200L)
                useCases.searchRecipeUseCase(text, recipes).let {
                    useCases.sortRecipeListUseCase(it, _recipeOrder.value)
                }
            }
        }
        .onEach { _isSearching.update { false } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _recipes.value
        )

    init {
        getInitRecipes()
    }

    fun onSearchTextChange(text: String) {
        _searchText.value = text
    }

    fun toggleOrderSection() {
        _isVisible.value = !_isVisible.value
    }

    fun onOrderChange(recipeOrder: RecipeOrder) {
        if (_recipeOrder.value::class == recipeOrder::class
            && _recipeOrder.value.orderType::class == recipeOrder.orderType::class
        ) {
            return
        }
        _recipeOrder.value = recipeOrder
        sortRecipes()
    }

    private fun sortRecipes() {
        viewModelScope.launch {
            _recipes.value = useCases.sortRecipeListUseCase(
                _recipes.value,
                _recipeOrder.value
            )
        }
    }

    private fun getInitRecipes() {
        useCases.getRecipeListUseCase().onEach { result ->
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
                    _recipes.value = result.data ?: emptyList()
                }
            }
        }.launchIn(viewModelScope)
    }
}