package com.example.m1_android_project.core.designsystem.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@ThemePreviews
@Composable
private fun ApercuPalette() = Planche {
    val c = TableTheme.couleurs
    Teinte("fond", c.fond, c.texte)
    Teinte("surface", c.surface, c.texte)
    Teinte("texte", c.texte, c.fond)
    Teinte("texteSecondaire", c.texteSecondaire, c.fond)
    Teinte("action", c.action, c.surAction)
    Teinte("teinteAction", c.teinteAction, c.action)
    Teinte("accent", c.accent, c.surAccent)
    Teinte("teinteAccent", c.teinteAccent, c.texte)
    Teinte("adapte", c.adapte, c.surAction)
    Teinte("teinteAdapte", c.teinteAdapte, c.adapte)
    Teinte("alcool", c.alcool, c.fond)
    Teinte("teinteAlcool", c.teinteAlcool, c.alcool)
    Trait("filet", c.filet, TableTheme.espacements.filet)
    Trait("filetFort", c.filetFort, TableTheme.espacements.filetFort)
}

@ThemePreviews
@Composable
private fun ApercuTypo() = Planche {
    val typo = TableTheme.typo
    Style("affiche", "Table", typo.affiche)
    Style("titre", "Salade de pâtes méditerranéenne", typo.titre)
    Style("section", "Sur l'étagère", typo.section)
    Style("valeur", "Vermentino", typo.valeur)
    Style("chiffre", "350 g · 25 min · 12,5 % vol.", typo.chiffre)
    Style("corps", "Portez une grande casserole d'eau salée à ébullition.", typo.corps)
    Style("secondaire", "Italie · Plaques seulement.", typo.secondaire)
    Style("libelle", "Tomates cerises", typo.libelle)
    Style("action", "Ajouter à la liste", typo.action)
    Style("badge", "25 min · sans four", typo.badge)
    Style("rubrique", "CARTE DU JEUDI 5 NOVEMBRE", typo.rubrique)
    Style("onglet", "Explorer", typo.onglet)
}

@ThemePreviews
@Composable
private fun ApercuEspacements() = Planche {
    val e = TableTheme.espacements
    Espace("xs", e.xs)
    Espace("s", e.s)
    Espace("m", e.m)
    Espace("l", e.l)
    Espace("marge", e.marge)
    Espace("xl", e.xl)
    Espace("xxl", e.xxl)
    Espace("cibleTactile", e.cibleTactile)
}

@ThemePreviews
@Composable
private fun ApercuFormes() = Planche {
    val f = TableTheme.formes
    Forme("case", f.case)
    Forme("puce", f.puce)
    Forme("carte", f.carte)
    Forme("bouton", f.bouton)
    Forme("photo", f.photo)
    Forme("feuille", f.feuille)
    Forme("badge", f.badge)
}

@Composable
internal fun Planche(contenu: @Composable () -> Unit) = TableTheme {
    Column(
        modifier = Modifier.width(390.dp).padding(TableTheme.espacements.marge),
        verticalArrangement = Arrangement.spacedBy(TableTheme.espacements.s),
    ) {
        contenu()
    }
}

@Composable
private fun Teinte(nom: String, couleur: Color, surCouleur: Color) {
    Text(
        text = nom,
        color = surCouleur,
        style = TableTheme.typo.libelle,
        modifier = Modifier
            .fillMaxWidth()
            .background(couleur, TableTheme.formes.carte)
            .border(TableTheme.espacements.filet, TableTheme.couleurs.filet, TableTheme.formes.carte)
            .padding(TableTheme.espacements.m),
    )
}

@Composable
private fun Trait(nom: String, couleur: Color, epaisseur: Dp) {
    Text(nom, style = TableTheme.typo.libelle)
    Box(Modifier.fillMaxWidth().height(epaisseur).background(couleur))
}

@Composable
private fun Style(nom: String, exemple: String, style: TextStyle) {
    Column {
        Text(nom, style = TableTheme.typo.onglet, color = TableTheme.couleurs.texteSecondaire)
        Text(exemple, style = style)
    }
}

@Composable
private fun Espace(nom: String, taille: Dp) = LigneApercu(nom) {
    Box(Modifier.size(width = taille, height = 16.dp).background(TableTheme.couleurs.action))
}

@Composable
private fun Forme(nom: String, forme: Shape) = LigneApercu(nom) {
    Box(Modifier.size(width = 120.dp, height = 56.dp).background(TableTheme.couleurs.action, forme))
}

@Composable
private fun LigneApercu(nom: String, exemple: @Composable () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(nom, Modifier.width(120.dp), style = TableTheme.typo.libelle)
        exemple()
    }
}
