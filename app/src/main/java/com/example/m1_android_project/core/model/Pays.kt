package com.example.m1_android_project.core.model

import java.util.Locale

/** Pays de TheMealDB, code ISO nul s'il n'est pas reconnu */
data class Pays(
    val code: String?,
    val nom: String,
    /** Valeur attendue par le filtre : Italian */
    val origineCulinaire: String,
)

/** IT → Italie */
fun nomPays(code: String): String =
    Locale.Builder().setRegion(code).build().getDisplayCountry(Locale.FRENCH)
