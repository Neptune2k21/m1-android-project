package com.example.m1_android_project.core.model

/** Contrat commun des éléments affichables dans une carte, un menu ou les favoris. */
interface ElementCarte {
    /** Identifiant préfixé par la source, par exemple mealdb:52771 ou vin:merlot. */
    val id: String
    val nom: String
    val photoUrl: String?
}

private val identifiantAvecSource = Regex("^[^:\\s]+:[^:\\s]+$")

internal fun verifierIdentite(id: String, nom: String) {
    require(identifiantAvecSource.matches(id)) {
        "L'identifiant doit contenir une source et une valeur, séparées par deux-points."
    }
    require(nom.isNotBlank()) { "Le nom doit être renseigné." }
}
