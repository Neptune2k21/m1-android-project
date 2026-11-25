package com.example.m1_android_project.core.designsystem.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.example.m1_android_project.R

private val SchibstedGrotesk = FontFamily(
    Font(R.font.schibsted_grotesk_bold, FontWeight.Bold),
    Font(R.font.schibsted_grotesk_extrabold, FontWeight.ExtraBold),
    Font(R.font.schibsted_grotesk_black, FontWeight.Black),
)

private val Onest = FontFamily(
    Font(R.font.onest_regular, FontWeight.Normal),
    Font(R.font.onest_medium, FontWeight.Medium),
    Font(R.font.onest_bold, FontWeight.Bold),
)

private val Base = TextStyle(
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None,
    ),
)

private val Titres = Base.copy(fontFamily = SchibstedGrotesk)
private val Textes = Base.copy(fontFamily = Onest)

/** Schibsted Grotesk pour les titres Onest pour le texte et les chiffres */
object Typo {
    /** Nom de l'application sur l'écran de lancement */
    val affiche = Titres.copy(
        fontWeight = FontWeight.Black,
        fontSize = 52.sp,
        lineHeight = 52.sp,
        letterSpacing = (-0.03).em,
    )

    /** Titre d'écran nom d'un plat en tête de fiche */
    val titre = Titres.copy(
        fontWeight = FontWeight.ExtraBold,
        fontSize = 34.sp,
        lineHeight = 35.sp,
        letterSpacing = (-0.02).em,
    )

    val section = Titres.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 28.sp,
        letterSpacing = (-0.01).em,
    )

    /** Valeur en toutes lettres */
    val valeur = Titres.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        letterSpacing = (-0.01).em,
    )

    /** Quantité ou durée  */
    val chiffre = Textes.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontFeatureSettings = "tnum",
    )

    val corps = Textes.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 15.sp,
        lineHeight = 23.sp,
    )

    /** Sous-titre, précision, mention. */
    val secondaire = Textes.copy(
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    )

    /** Nom d'une ligne */
    val libelle = Textes.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
    )

    /** Bouton */
    val action = Textes.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 15.sp,
        lineHeight = 20.sp,
    )

    val badge = Textes.copy(
        fontWeight = FontWeight.Bold,
        fontSize = 12.5.sp,
        lineHeight = 16.sp,
    )

    /** Intitulé à afficher en capitales */
    val rubrique = Textes.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.06.em,
    )

    /** Barre de navigation */
    val onglet = Textes.copy(
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp,
    )
}

internal val TypoMaterial = Typography(
    displayLarge = Typo.affiche,
    displayMedium = Typo.affiche,
    displaySmall = Typo.titre,
    headlineLarge = Typo.titre,
    headlineMedium = Typo.section,
    headlineSmall = Typo.section,
    titleLarge = Typo.section,
    titleMedium = Typo.valeur,
    titleSmall = Typo.libelle,
    bodyLarge = Typo.corps,
    bodyMedium = Typo.secondaire,
    bodySmall = Typo.secondaire,
    labelLarge = Typo.action,
    labelMedium = Typo.badge,
    labelSmall = Typo.onglet,
)
