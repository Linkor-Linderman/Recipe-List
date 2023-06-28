package com.example.recipe_list.data.remote.dto

import com.example.recipe_list.domain.model.RecipeBriefInfo

data class RecipeBriefInfoDTO(
    val name: String,
    val uuid: String
) {
    fun toRecipeBriefInfo(): RecipeBriefInfo =
        RecipeBriefInfo(name, uuid)
}
