package com.example.m1_android_project.core.model

/** Résultat d'un filtre : la fiche complète s'obtient ensuite par son id */
data class Apercu(
    override val id: String,
    override val nom: String,
    override val photoUrl: String? = null,
) : ElementCarte {
    init {
        verifierIdentite(id, nom)
    }
}
