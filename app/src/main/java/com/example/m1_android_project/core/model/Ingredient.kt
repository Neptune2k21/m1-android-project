package com.example.m1_android_project.core.model

data class Ingredient(
    val nom: String,
    val quantite: Quantite = Quantite.NonRenseignee,
) {
    init {
        require(nom.isNotBlank()) { "Le nom de l'ingrédient doit être renseigné." }
    }
}
