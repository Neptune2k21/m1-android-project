package com.example.m1_android_project.core.network.mealdb

import com.example.m1_android_project.core.model.Apercu
import com.example.m1_android_project.core.model.Categorie
import com.example.m1_android_project.core.model.Pays
import com.example.m1_android_project.core.model.Recette
import com.example.m1_android_project.core.model.nomPays
import com.example.m1_android_project.core.network.decouperEtapes
import com.example.m1_android_project.core.network.identifiant
import com.example.m1_android_project.core.network.texte
import com.example.m1_android_project.core.network.versIngredients

internal const val SOURCE_MEALDB = "mealdb"

// Un élément sans id ni nom est ignoré plutôt que de faire échouer toute la liste

internal fun ReponseRepas.recettes(): List<Recette> = meals.orEmpty().mapNotNull { it.versRecette() }

internal fun ReponseResumes.apercus(): List<Apercu> = meals.orEmpty().mapNotNull { it.versApercu() }

internal fun ReponseCategories.categories(): List<Categorie> =
    categories.orEmpty().mapNotNull { dto -> dto.strCategory.texte()?.let { Categorie(it, dto.strCategoryThumb.texte()) } }

internal fun ReponsePays.pays(): List<Pays> = meals.orEmpty().mapNotNull { it.versPays() }

private fun RepasDto.versRecette(): Recette? {
    val id = identifiant(SOURCE_MEALDB, idMeal) ?: return null
    val nom = strMeal.texte() ?: return null
    return Recette(
        id = id,
        nom = nom,
        ingredients = ingredients.versIngredients(),
        etapes = decouperEtapes(strInstructions),
        photoUrl = strMealThumb.texte(),
        pays = CodesPays.code(strCountry),
        origineCulinaire = strArea.texte(),
        categorie = strCategory.texte(),
    )
}

private fun ResumeRepasDto.versApercu(): Apercu? {
    val id = identifiant(SOURCE_MEALDB, idMeal) ?: return null
    val nom = strMeal.texte() ?: return null
    return Apercu(id, nom, strMealThumb.texte())
}

private fun PaysDto.versPays(): Pays? {
    val origine = strArea.texte() ?: return null
    val anglais = strCountry.texte() ?: return null
    val code = CodesPays.code(anglais)
    return Pays(code, code?.let(::nomPays) ?: anglais, origine)
}
