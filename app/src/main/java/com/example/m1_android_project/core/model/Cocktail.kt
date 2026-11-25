package com.example.m1_android_project.core.model

/** Les doses correspondent à une préparation de référence. */
data class Cocktail(
    override val id: String,
    override val nom: String,
    override val contientAlcool: Boolean,
    val ingredients: List<Ingredient>,
    val etapes: List<String>,
    override val photoUrl: String? = null,
    val verre: String? = null,
) : Boisson {
    init {
        verifierIdentite(id, nom)
        require(etapes.all { it.isNotBlank() }) { "Les étapes doivent contenir du texte." }
    }
}
