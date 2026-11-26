package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Plat, verre ou dessert de la carte, avec son numéro de service */
@Composable
fun LigneNumerotee(
    libelle: String,
    valeur: String,
    modifier: Modifier = Modifier,
    numero: Int? = null,
    detail: String? = null,
    onClick: (() -> Unit)? = null,
) {
    LignePointillee(
        libelle = libelle,
        modifier = if (onClick != null) modifier.clickable(onClick = onClick) else modifier,
        detail = detail,
        debut = numero?.let { { Pastille(it) } },
        fin = { Text(valeur, color = TableTheme.couleurs.texte, style = TableTheme.typo.valeur) },
    )
}

@Composable
private fun Pastille(numero: Int) {
    Box(
        modifier = Modifier
            .sizeIn(minWidth = TaillePastille, minHeight = TaillePastille)
            .background(TableTheme.couleurs.action, TableTheme.formes.badge),
        contentAlignment = Alignment.Center,
    ) {
        Text("$numero", color = TableTheme.couleurs.surAction, style = TableTheme.typo.badge)
    }
}

private val TaillePastille = 24.dp

@ThemePreviews
@Composable
private fun ApercuLigneNumerotee() = Planche {
    Column {
        LigneNumerotee("Le verre", "Vermentino", numero = 2)
        LigneNumerotee("Le dessert", "Affogato", numero = 3)
        LigneNumerotee("Vermentino", "12,5 % vol.", numero = 4, detail = "Blanc sec, iodé, servi à 10 °C")
        LigneNumerotee("Italie", "Penne all'arrabbiata", onClick = {})
        LigneNumerotee("Huile d'olive vierge extra de Toscane, première pression", "3 c. à s.")
    }
}
