package com.example.recipe_list.domain.useCase

import com.example.recipe_list.R
import com.example.recipe_list.common.Resource
import com.example.recipe_list.common.StringResourcesManager
import com.example.recipe_list.domain.model.RecipeInfo
import com.example.recipe_list.domain.repository.RecipeRepository
import com.example.recipe_list.domain.utill.OrderType
import com.example.recipe_list.domain.utill.RecipeOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class GetRecipeListUseCase(
    private val repository: RecipeRepository,
    private val stringResourcesManager: StringResourcesManager
) {
    private val unexpectedErrorMessage =
        stringResourcesManager.getStringResourceById(R.string.unexpected_error)
    private val connectionErrorMessage =
        stringResourcesManager.getStringResourceById(R.string.no_connection_error)

    operator fun invoke(): Flow<Resource<List<RecipeInfo>>> =
        flow {
            try {
                emit(Resource.Loading())
                val recipes = repository.getRecipes()
                emit(Resource.Success(recipes))
            } catch (e: HttpException) {
                emit(
                    Resource.Error(
                        e.localizedMessage ?: unexpectedErrorMessage
                    )
                )
            } catch (e: IOException) {
                emit(Resource.Error(connectionErrorMessage))
            }
        }.flowOn(Dispatchers.IO)

}