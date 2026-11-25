package com.example.m1_android_project.core.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

//passer par Couleurs, jamais directement.
private val Blanc = Color(0xFFFFFFFF)
private val Brume = Color(0xFFF2F5F1)
private val Encre = Color(0xFF0F1F17)
private val Herbe = Color(0xFF16764A)
private val Agrume = Color(0xFFFFB020)
private val Sauge = Color(0xFF55635B)
private val Bordeaux = Color(0xFF5E1F2B)
private val BordeauxPale = Color(0xFFEFDFE0)

private val Nuit = Color(0xFF0A1410)
private val SurfaceNuit = Color(0xFF12201A)
private val EncreNuit = Color(0xFFEAF2EC)
private val HerbeNuit = Color(0xFF4FBF84)
private val AgrumeNuit = Color(0xFFFFC247)
private val SaugeNuit = Color(0xFF9DB1A5)
private val BordeauxNuit = Color(0xFFD08B96)
private val BordeauxPaleNuit = Color(0xFF33201F)

/**
 * Couleurs de l'application, nommées selon leur rôle.
 *
 * Les teintes sont les fonds pâles des badges et encarts ; le texte posé dessus prend la couleur
 * du même nom ou [texte].
 */
@Immutable
data class Couleurs(
    val fond: Color,
    val surface: Color,
    val texte: Color,
    val texteSecondaire: Color,
    val action: Color,
    val surAction: Color,
    val teinteAction: Color,
    val accent: Color,
    val surAccent: Color,
    val teinteAccent: Color,
    /** Réservé à ce qui convient au profil. */
    val adapte: Color,
    val teinteAdapte: Color,
    /** Réservé à l'alcool. */
    val alcool: Color,
    val teinteAlcool: Color,
    val filet: Color,
    val filetFort: Color,
)

internal val CouleursClaires = Couleurs(
    fond = Blanc,
    surface = Brume,
    texte = Encre,
    texteSecondaire = Sauge,
    action = Herbe,
    surAction = Blanc,
    teinteAction = Herbe.teinte(0.10f, Blanc),
    accent = Agrume,
    surAccent = Encre,
    teinteAccent = Agrume.teinte(0.18f, Blanc),
    adapte = Herbe,
    teinteAdapte = Herbe.teinte(0.10f, Blanc),
    alcool = Bordeaux,
    teinteAlcool = BordeauxPale,
    filet = Encre.copy(alpha = 0.12f),
    filetFort = Encre,
)

internal val CouleursSombres = Couleurs(
    fond = Nuit,
    surface = SurfaceNuit,
    texte = EncreNuit,
    texteSecondaire = SaugeNuit,
    action = HerbeNuit,
    surAction = Nuit,
    teinteAction = HerbeNuit.teinte(0.18f, Nuit),
    accent = AgrumeNuit,
    surAccent = Encre,
    teinteAccent = AgrumeNuit.teinte(0.16f, Nuit),
    adapte = HerbeNuit,
    teinteAdapte = HerbeNuit.teinte(0.18f, Nuit),
    alcool = BordeauxNuit,
    teinteAlcool = BordeauxPaleNuit,
    filet = EncreNuit.copy(alpha = 0.12f),
    filetFort = EncreNuit,
)

internal val LocalCouleurs = staticCompositionLocalOf { CouleursClaires }

// Opaque, pour rester lisible au-dessus d'une photo.
private fun Color.teinte(part: Float, fond: Color) = copy(alpha = part).compositeOver(fond)

/** Habille les composants Material avec la palette. */
internal fun Couleurs.versMaterial(sombre: Boolean): ColorScheme {
    val base = if (sombre) darkColorScheme() else lightColorScheme()
    return base.copy(
        primary = action,
        onPrimary = surAction,
        primaryContainer = teinteAction,
        onPrimaryContainer = texte,
        inversePrimary = if (sombre) Herbe else HerbeNuit,
        secondary = texteSecondaire,
        onSecondary = fond,
        secondaryContainer = teinteAction,
        onSecondaryContainer = texte,
        tertiary = accent,
        onTertiary = surAccent,
        tertiaryContainer = teinteAccent,
        onTertiaryContainer = texte,
        background = fond,
        onBackground = texte,
        surface = fond,
        onSurface = texte,
        surfaceVariant = surface,
        onSurfaceVariant = texteSecondaire,
        surfaceTint = action,
        inverseSurface = texte,
        inverseOnSurface = fond,
        outline = texteSecondaire,
        outlineVariant = filet,
        surfaceBright = fond,
        surfaceDim = surface,
        surfaceContainerLowest = fond,
        surfaceContainerLow = surface,
        surfaceContainer = surface,
        surfaceContainerHigh = surface,
        surfaceContainerHighest = surface,
    )
}
