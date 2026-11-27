package com.example.m1_android_project.core.navigation

import kotlinx.serialization.Serializable

/** les cinq onglets */
object Routes {
    @Serializable data object Carte

    @Serializable data object Explorer

    @Serializable data object Accords

    @Serializable data object Courses

    @Serializable data object Favoris

    /** id au format mealdb:52777 */
    @Serializable data class Fiche(val id: String)
}
