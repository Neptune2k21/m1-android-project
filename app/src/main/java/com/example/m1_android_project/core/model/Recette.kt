package com.example.m1_android_project.core.model

/** Fiche détaillée. Les listes sont fournies explicitement par la source de données. */
data class Recette(
    override val id: String,
    override val nom: String,
    val ingredients: List<Ingredient>,
    val etapes: List<String>,
    override val photoUrl: String? = null,
    val pays: String? = null,
    val origineCulinaire: String? = null,
    val categorie: String? = null,
    val portionsBase: Int = 4,
    /** null signifie inconnu, un ensemble vide signifie aucun matériel identifié. */
    val materiel: Set<Equipement>? = null,
    val dureeMinutes: Int? = null,
) : ElementCarte {
    init {
        verifierIdentite(id, nom)
        require(portionsBase > 0) { "Le nombre de portions de base doit être positif." }
        require(dureeMinutes == null || dureeMinutes >= 0) { "La durée doit être positive ou nulle." }
        require(etapes.all { it.isNotBlank() }) { "Les étapes doivent contenir du texte." }
    }
}
