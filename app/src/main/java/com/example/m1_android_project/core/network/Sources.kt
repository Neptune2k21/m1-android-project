package com.example.m1_android_project.core.network

import java.net.URLEncoder

/** Toutes les adresses externes de l'app */
object Sources {
    // urls de l'API MealDB et CocktailDB
    const val URL_MEALDB = "https://www.themealdb.com/api/json/v1/1/"
    const val URL_COCKTAILDB = "https://www.thecocktaildb.com/api/json/v1/1/"

    /** PNG détouré de 200 px */
    fun imageIngredient(nom: String) =
        "https://www.themealdb.com/images/ingredients/${encoder(nom)}-small.png"

    /** Drapeau en 3:2 recadré par la tuile */
    fun drapeau(codePays: String) =
        "https://flags.restcountries.com/v5/w160/${codePays.lowercase()}.png"

    // La surcharge avec Charset demande l'API 33
    private fun encoder(texte: String) = URLEncoder.encode(texte.trim(), "UTF-8").replace("+", "%20")
}
