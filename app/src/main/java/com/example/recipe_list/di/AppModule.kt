package com.example.recipe_list.di

import android.content.Context
import com.example.recipe_list.common.Constants
import com.example.recipe_list.common.StringResourcesManager
import com.example.recipe_list.data.remote.RecipeApi
import com.example.recipe_list.data.repository.ImageRepositoryImpl
import com.example.recipe_list.data.repository.RecipeRepositoryImpl
import com.example.recipe_list.domain.repository.ImageRepository
import com.example.recipe_list.domain.repository.RecipeRepository
import com.example.recipe_list.domain.useCase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRecipeApi(): RecipeApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RecipeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRecipeRepository(api: RecipeApi): RecipeRepository {
        return RecipeRepositoryImpl(api)
    }

    @Provides
    @Singleton
    fun provideImageRepository(
        @ApplicationContext context: Context
    ): ImageRepository {
        return ImageRepositoryImpl(context)
    }

    @Provides
    @Singleton
    fun provideImageUseCases(
        repository: ImageRepository,
        stringResourcesManager: StringResourcesManager
    ): DownloadImageUseCase {
        return DownloadImageUseCase(
            imageRepository = repository,
            stringResourcesManager = stringResourcesManager
        )
    }

    @Provides
    @Singleton
    fun provideRecipeUseCases(
        repository: RecipeRepository,
        stringResourcesManager: StringResourcesManager
    ): RecipeUseCases {
        return RecipeUseCases(
            getRecipeListUseCase = GetRecipeListUseCase(repository, stringResourcesManager),
            sortRecipeListUseCase = SortRecipeListUseCase(),
            searchRecipeUseCase = SearchRecipeUseCase(),
            getRecipeDetailsUseCase = GetRecipeDetailsUseCase(repository, stringResourcesManager)
        )
    }
}