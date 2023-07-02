package com.example.recipe_list.domain.useCase

import com.example.recipe_list.R
import com.example.recipe_list.common.Resource
import com.example.recipe_list.common.StringResourcesManager
import com.example.recipe_list.domain.model.RecipeDetails
import com.example.recipe_list.domain.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException

class GetRecipeDetailsUseCase(
    private val repository: RecipeRepository,
    private val stringResourcesManager: StringResourcesManager
) {
    private val unexpectedErrorMessage =
        stringResourcesManager.getStringResourceById(R.string.unexpected_error)
    private val connectionErrorMessage =
        stringResourcesManager.getStringResourceById(R.string.no_connection_error)

    operator fun invoke(
        id: String
    ): Flow<Resource<RecipeDetails>> =
        flow {
            try {
                emit(Resource.Loading())
                val recipe = repository.getRecipeDetails(id)
                emit(Resource.Success(recipe))
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