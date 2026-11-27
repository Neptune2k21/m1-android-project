package com.example.m1_android_project

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.m1_android_project.accords.AccordsEcran
import com.example.m1_android_project.carte.CarteEcran
import com.example.m1_android_project.core.designsystem.composant.BarreNavigation
import com.example.m1_android_project.core.designsystem.composant.ElementBarre
import com.example.m1_android_project.core.designsystem.icone.IconeAccords
import com.example.m1_android_project.core.designsystem.icone.IconeCarte
import com.example.m1_android_project.core.designsystem.icone.IconeCourses
import com.example.m1_android_project.core.designsystem.icone.IconeExplorer
import com.example.m1_android_project.core.designsystem.icone.IconeFavoris
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.navigation.LocalPorteeEcran
import com.example.m1_android_project.core.navigation.LocalRetoucheOnglet
import com.example.m1_android_project.core.navigation.LocalTransitionPartagee
import com.example.m1_android_project.core.navigation.Onglet
import com.example.m1_android_project.core.navigation.Routes
import com.example.m1_android_project.core.navigation.allerA
import com.example.m1_android_project.core.navigation.onglet
import com.example.m1_android_project.courses.CoursesEcran
import com.example.m1_android_project.explorer.ExplorerEcran
import com.example.m1_android_project.favoris.FavorisEcran
import com.example.m1_android_project.recette.FicheRecetteEcran
import kotlinx.coroutines.flow.MutableSharedFlow

/** Les cinq onglets et ce qui s'ouvre par-dessus. La barre n'existe que sur un onglet */
@Composable
fun TableApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    val entree by navController.currentBackStackEntryAsState()
    val onglet = entree?.destination.onglet()
    // La barre garde son onglet pendant qu'elle descend
    val dernierOnglet = remember { mutableStateOf(Onglet.Carte) }
    if (onglet != null) dernierOnglet.value = onglet
    val elements = Onglet.entries.map { ElementBarre(stringResource(it.libelle), it.icone()) }
    val retouches = remember { MutableSharedFlow<Unit>(extraBufferCapacity = 1) }

    Scaffold(
        modifier = modifier,
        containerColor = TableTheme.couleurs.fond,
        bottomBar = {
            AnimatedVisibility(
                visible = onglet != null,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                BarreNavigation(
                    elements = elements,
                    selection = dernierOnglet.value.ordinal,
                    onSelection = { index ->
                        val choisi = Onglet.entries[index]
                        if (choisi == onglet) retouches.tryEmit(Unit) else navController.allerA(choisi)
                    },
                )
            }
        },
    ) { marges ->
        SharedTransitionLayout(Modifier.padding(marges)) {
            CompositionLocalProvider(
                LocalTransitionPartagee provides this,
                LocalRetoucheOnglet provides retouches,
            ) {
                NavHost(
                    navController = navController,
                    startDestination = Routes.Carte,
                    enterTransition = { Fondu.entree },
                    exitTransition = { Fondu.sortie },
                    popEnterTransition = { Fondu.entree },
                    popExitTransition = { Fondu.sortie },
                ) {
                    ecran<Routes.Carte> { CarteEcran(onOuvrirFiche = { navController.navigate(Routes.Fiche(it)) }) }
                    ecran<Routes.Explorer> { ExplorerEcran() }
                    ecran<Routes.Accords> { AccordsEcran() }
                    ecran<Routes.Courses> { CoursesEcran() }
                    ecran<Routes.Favoris> { FavorisEcran() }
                    // La photo vole jusqu'à l'en-tête pendant que le reste apparaît autour
                    ecran<Routes.Fiche>(
                        entree = { fadeIn(tween(260, delayMillis = 120)) },
                        // Suivie au doigt pendant le retour prédictif
                        sortieRetour = { fadeOut(tween(260)) + scaleOut(targetScale = .9f) },
                    ) { entree ->
                        FicheRecetteEcran(id = entree.toRoute<Routes.Fiche>().id, onRetour = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

/** Destination qui donne sa portée d'animation à la photo partagée */
private inline fun <reified T : Any> NavGraphBuilder.ecran(
    noinline entree: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition)? = null,
    noinline sortieRetour: (AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition)? = null,
    crossinline contenu: @Composable (NavBackStackEntry) -> Unit,
) = composable<T>(enterTransition = entree, popExitTransition = sortieRetour) { entreePile ->
    CompositionLocalProvider(LocalPorteeEcran provides this) { contenu(entreePile) }
}

private fun Onglet.icone(): @Composable (Boolean) -> Unit = when (this) {
    Onglet.Carte -> { actif -> IconeCarte(actif) }
    Onglet.Explorer -> { actif -> IconeExplorer(actif) }
    Onglet.Accords -> { actif -> IconeAccords(actif) }
    Onglet.Courses -> { actif -> IconeCourses(actif) }
    Onglet.Favoris -> { actif -> IconeFavoris(actif) }
}

/** L'écran sortant s'efface, puis l'entrant se pose : aucun ordre suggéré entre les onglets */
private object Fondu {
    val entree = fadeIn(tween(220, delayMillis = 90)) + scaleIn(tween(220, delayMillis = 90), initialScale = 0.97f)
    val sortie = fadeOut(tween(90))
}
