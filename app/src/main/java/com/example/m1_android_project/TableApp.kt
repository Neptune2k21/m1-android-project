package com.example.m1_android_project

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.navigation.Onglet
import com.example.m1_android_project.core.navigation.Routes
import com.example.m1_android_project.core.navigation.allerA
import com.example.m1_android_project.core.navigation.onglet
import com.example.m1_android_project.courses.CoursesEcran
import com.example.m1_android_project.explorer.ExplorerEcran
import com.example.m1_android_project.favoris.FavorisEcran
import com.example.m1_android_project.recette.FicheRecetteEcran

/** Les cinq onglets et ce qui s'ouvre par-dessus. La barre n'existe que sur un onglet */
@Composable
fun TableApp(modifier: Modifier = Modifier, navController: NavHostController = rememberNavController()) {
    val entree by navController.currentBackStackEntryAsState()
    val onglet = entree?.destination.onglet()
    // La barre garde son onglet pendant qu'elle descend
    val dernierOnglet = remember { mutableStateOf(Onglet.Carte) }
    if (onglet != null) dernierOnglet.value = onglet
    val elements = Onglet.entries.map { ElementBarre(stringResource(it.libelle)) }

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
                    onSelection = { navController.allerA(Onglet.entries[it]) },
                )
            }
        },
    ) { marges ->
        NavHost(
            navController = navController,
            startDestination = Routes.Carte,
            modifier = Modifier.padding(marges),
            enterTransition = { Fondu.entree },
            exitTransition = { Fondu.sortie },
            popEnterTransition = { Fondu.entree },
            popExitTransition = { Fondu.sortie },
        ) {
            composable<Routes.Carte> { CarteEcran(onOuvrirFiche = { navController.navigate(Routes.Fiche(it)) }) }
            composable<Routes.Explorer> { ExplorerEcran() }
            composable<Routes.Accords> { AccordsEcran() }
            composable<Routes.Courses> { CoursesEcran() }
            composable<Routes.Favoris> { FavorisEcran() }
            composable<Routes.Fiche> { entree ->
                FicheRecetteEcran(id = entree.toRoute<Routes.Fiche>().id, onRetour = { navController.popBackStack() })
            }
        }
    }
}

/** L'écran sortant s'efface, puis l'entrant se pose : aucun ordre suggéré entre les onglets */
private object Fondu {
    val entree = fadeIn(tween(220, delayMillis = 90)) + scaleIn(tween(220, delayMillis = 90), initialScale = 0.97f)
    val sortie = fadeOut(tween(90))
}
