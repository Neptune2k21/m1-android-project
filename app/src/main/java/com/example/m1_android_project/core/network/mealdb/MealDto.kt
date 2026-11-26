package com.example.m1_android_project.core.network.mealdb

import com.example.m1_android_project.core.network.AvecIngredients
import com.example.m1_android_project.core.network.IngredientDto
import kotlinx.serialization.Serializable

// « meals: null » quand rien ne correspond, d'où les listes nullables

@Serializable
data class ReponseRepas(val meals: List<@Serializable(with = RepasSerializer::class) RepasDto>? = null)

@Serializable
data class ReponseResumes(val meals: List<ResumeRepasDto>? = null)

@Serializable
data class ReponseCategories(val categories: List<CategorieDto>? = null)

@Serializable
data class ReponsePays(val meals: List<PaysDto>? = null)

@Serializable
data class RepasDto(
    val idMeal: String? = null,
    val strMeal: String? = null,
    val strCategory: String? = null,
    val strArea: String? = null,
    val strCountry: String? = null,
    val strInstructions: String? = null,
    val strMealThumb: String? = null,
    val ingredients: List<IngredientDto> = emptyList(),
)

object RepasSerializer : AvecIngredients<RepasDto>(RepasDto.serializer())

@Serializable
data class ResumeRepasDto(
    val idMeal: String? = null,
    val strMeal: String? = null,
    val strMealThumb: String? = null,
)

@Serializable
data class CategorieDto(
    val strCategory: String? = null,
    val strCategoryThumb: String? = null,
)

@Serializable
data class PaysDto(
    val strArea: String? = null,
    val strCountry: String? = null,
)
