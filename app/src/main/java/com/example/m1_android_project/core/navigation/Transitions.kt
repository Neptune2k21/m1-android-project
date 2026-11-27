package com.example.m1_android_project.core.navigation

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

/** Portées fournies par TableApp, nulles dans les aperçus */
val LocalTransitionPartagee = staticCompositionLocalOf<SharedTransitionScope?> { null }
val LocalPorteeEcran = staticCompositionLocalOf<AnimatedVisibilityScope?> { null }

/** Touches sur l'onglet déjà affiché */
val LocalRetoucheOnglet = staticCompositionLocalOf<Flow<Unit>> { emptyFlow() }

/** La photo d'un plat grandit de sa carte jusqu'à l'en-tête de sa fiche */
@Composable
fun Modifier.photoPartagee(id: String): Modifier {
    val transition = LocalTransitionPartagee.current ?: return this
    val portee = LocalPorteeEcran.current ?: return this
    return with(transition) {
        this@photoPartagee.sharedElement(rememberSharedContentState("photo:$id"), animatedVisibilityScope = portee)
    }
}

/** Remonte en haut de la liste quand on touche à nouveau l'onglet */
@Composable
fun RemonterAuRetouche(liste: LazyListState) {
    val retouches = LocalRetoucheOnglet.current
    LaunchedEffect(liste, retouches) {
        retouches.collect { liste.animateScrollToItem(0) }
    }
}
