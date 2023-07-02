package com.example.recipe_list.domain.useCase

import com.example.recipe_list.R
import com.example.recipe_list.common.Resource
import com.example.recipe_list.common.StringResourcesManager
import com.example.recipe_list.domain.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.IOException

class DownloadImageUseCase(
    private val imageRepository: ImageRepository,
    private val stringResourcesManager: StringResourcesManager
) {

    operator fun invoke(url: String, fileName: String): Flow<Resource<Unit>> =
        flow {
            try {
                emit(Resource.Loading())
                imageRepository.downloadImage(url, fileName)
                emit(Resource.Success(Unit))
            } catch (e: IOException) {
                emit(
                    Resource.Error(
                        e.localizedMessage ?: stringResourcesManager.getStringResourceById(
                            R.string.unexpected_error
                        )
                    )
                )
            }
        }.flowOn(Dispatchers.IO)
}