package com.example.m1_android_project.core.network.cocktaildb

import com.example.m1_android_project.core.model.Apercu
import com.example.m1_android_project.core.model.Cocktail
import com.example.m1_android_project.core.network.decouperPhrases
import com.example.m1_android_project.core.network.identifiant
import com.example.m1_android_project.core.network.texte
import com.example.m1_android_project.core.network.versIngredients

internal const val SOURCE_COCKTAILDB = "cocktaildb"

// Un élément sans id ni nom est ignoré plutôt que de faire échouer toute la liste

internal fun ReponseBoissons.cocktails(): List<Cocktail> = drinks.mapNotNull { it.versCocktail() }

internal fun ReponseResumesBoissons.apercus(): List<Apercu> = drinks.mapNotNull { it.versApercu() }

internal fun ReponseVerres.verres(): List<String> = drinks.orEmpty().mapNotNull { it.strGlass.texte() }.distinct()

private fun BoissonDto.versCocktail(): Cocktail? {
    val id = identifiant(SOURCE_COCKTAILDB, idDrink) ?: return null
    val nom = strDrink.texte() ?: return null
    return Cocktail(
        id = id,
        nom = nom,
        contientAlcool = contientAlcool(strAlcoholic),
        ingredients = ingredients.versIngredients(),
        etapes = decouperPhrases(strInstructions),
        photoUrl = strDrinkThumb.texte(),
        verre = strGlass.texte(),
    )
}

/** Dans le doute on bloque ->Optional alcohol ou valeur absente comptent comme alcoolisés */
internal fun contientAlcool(strAlcoholic: String?): Boolean =
    !strAlcoholic.texte().equals("Non alcoholic", ignoreCase = true)

private fun ResumeBoissonDto.versApercu(): Apercu? {
    val id = identifiant(SOURCE_COCKTAILDB, idDrink) ?: return null
    val nom = strDrink.texte() ?: return null
    return Apercu(id, nom, strDrinkThumb.texte())
}
