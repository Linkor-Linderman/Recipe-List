package com.example.recipe_list.domain.repository

interface ImageRepository {
    suspend fun downloadImage(url: String, fileName: String)
}