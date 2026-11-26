package com.example.m1_android_project.core.model

enum class Majorite { NonRenseignee, Mineur, Majeur }

enum class PreferenceBoissons { SansAlcool, VinsEtCocktails }

enum class Regime { DeTout, Vegetarien, Vegetalien, Pescetarien, SansGluten, SansLactose }

enum class Equipement { Plaques, Four, MicroOndes, AirFryer, Mixeur, Grill }

/** Profil local et préférences, indépendants de leur stockage. */
data class Profil(
    val majorite: Majorite = Majorite.NonRenseignee,
    val preferenceBoissons: PreferenceBoissons = PreferenceBoissons.SansAlcool,
    val regimes: Set<Regime> = emptySet(),
    val equipements: Set<Equipement> = emptySet(),
    val prenom: String? = null,
    val avatar: String? = null,
)
