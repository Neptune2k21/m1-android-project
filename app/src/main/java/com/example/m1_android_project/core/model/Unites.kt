package com.example.m1_android_project.core.model

import java.util.Locale

private val espaces = Regex("[\\s\\p{Zs}]+")
private val aliasUnites = listOf(
    listOf("g", "gram", "grams", "gramme", "grammes"),
    listOf("kg", "kilogram", "kilograms", "kilogramme", "kilogrammes"),
    listOf("mg", "milligram", "milligrams", "milligramme", "milligrammes"),
    listOf("ml", "milliliter", "milliliters", "millilitre", "millilitres"),
    listOf("cl", "centiliter", "centiliters", "centilitre", "centilitres"),
    listOf("dl", "deciliter", "deciliters", "décilitre", "décilitres"),
    listOf("l", "liter", "liters", "litre", "litres"),
    listOf("tsp", "teaspoon", "teaspoons", "cuillère à café", "cuillères à café", "c. à café"),
    listOf("tbsp", "tablespoon", "tablespoons", "cuillère à soupe", "cuillères à soupe", "c. à soupe"),
    listOf("cup", "cups"),
    listOf("oz", "ounce", "ounces"),
    listOf("fl oz", "fluid ounce", "fluid ounces"),
    listOf("lb", "lbs", "pound", "pounds"),
    listOf("piece", "pieces", "pièce", "pièces"),
).flatMap { variantes -> variantes.map { it to variantes.first() } }.toMap()

internal fun normaliserEspaces(texte: String): String = texte.replace(espaces, " ").trim()

internal fun normaliserUnite(unite: String?): String? {
    val libelle = unite?.let(::normaliserEspaces)?.lowercase(Locale.ROOT)?.ifEmpty { null }
    return aliasUnites[libelle] ?: libelle
}

internal fun estUniteConnue(unite: String): Boolean = unite in aliasUnites.values
