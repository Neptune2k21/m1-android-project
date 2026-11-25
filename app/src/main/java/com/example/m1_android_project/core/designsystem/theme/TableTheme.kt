package com.example.m1_android_project.core.designsystem.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember

@Composable
fun TableTheme(
    sombre: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val couleurs = if (sombre) CouleursSombres else CouleursClaires

    MaterialTheme(
        colorScheme = remember(couleurs) { couleurs.versMaterial(sombre) },
        typography = TypoMaterial,
        shapes = FormesMaterial,
    ) {
        CompositionLocalProvider(
            LocalCouleurs provides couleurs,
            // Sans Surface parente le texte resterait noir en sombre
            LocalContentColor provides couleurs.texte,
            content = content,
        )
    }
}

/** Point d'accès unique au thème : `TableTheme.couleurs.action` `TableTheme.typo.titre`… */
object TableTheme {
    val couleurs: Couleurs
        @Composable @ReadOnlyComposable get() = LocalCouleurs.current

    val typo get() = Typo
    val espacements get() = Espacements
    val formes get() = Formes
}
