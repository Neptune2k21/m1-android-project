package com.example.m1_android_project.core.navigation

import androidx.annotation.StringRes
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.m1_android_project.R

/** Onglets de la barre dans leur ordre d'affichage */
enum class Onglet(val route: Any, @StringRes val libelle: Int) {
    Carte(Routes.Carte, R.string.onglet_carte),
    Explorer(Routes.Explorer, R.string.onglet_explorer),
    Accords(Routes.Accords, R.string.onglet_accords),
    Courses(Routes.Courses, R.string.onglet_courses),
    Favoris(Routes.Favoris, R.string.onglet_favoris),
}

/** Chaque onglet garde sa pile et son défilement quand on le quitte */
fun NavController.allerA(onglet: Onglet) = navigate(onglet.route) {
    popUpTo(graph.findStartDestination().id) { saveState = true }
    launchSingleTop = true
    restoreState = true
}

/** L'onglet affiché ou null sur un écran ouvert par-dessus */
fun NavDestination?.onglet(): Onglet? =
    Onglet.entries.firstOrNull { onglet -> this?.hierarchy?.any { it.hasRoute(onglet.route::class) } == true }
