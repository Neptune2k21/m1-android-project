package com.example.m1_android_project.core.network.mealdb

import java.util.Locale

/** strCountry vers code ISO par le nom anglais exact : Morocco → MA, Mali → ML */
internal object CodesPays {

    // Noms que Locale écrit autrement
    private val exceptions = mapOf(
        "Antigua and Barbuda" to "AG",
        "Bosnia and Herzegovina" to "BA",
        "Czechia" to "CZ",
        "DR Congo" to "CD",
        "Hong Kong" to "HK",
        "Ivory Coast" to "CI",
        "Kosovo" to "XK",
        "Myanmar" to "MM",
        "North Macedonia" to "MK",
        "Palestine" to "PS",
        "Republic of the Congo" to "CG",
        "Saint Lucia" to "LC",
        "Trinidad and Tobago" to "TT",
        "Turkey" to "TR",
    )

    private val parNomAnglais: Map<String, String> by lazy {
        Locale.getISOCountries().associateBy { code ->
            Locale.Builder().setRegion(code).build().getDisplayCountry(Locale.ENGLISH).lowercase(Locale.ROOT)
        }
    }

    fun code(pays: String?): String? {
        val nom = pays?.trim()?.ifEmpty { null } ?: return null
        return exceptions[nom] ?: parNomAnglais[nom.lowercase(Locale.ROOT)]
    }
}
