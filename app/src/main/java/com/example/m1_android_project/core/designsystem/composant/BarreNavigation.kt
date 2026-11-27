package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Un onglet de la barre */
data class ElementBarre(val libelle: String)

/** Barre du bas. L'onglet actif porte le repère */
@Composable
fun BarreNavigation(
    elements: List<ElementBarre>,
    selection: Int,
    onSelection: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val couleurs = TableTheme.couleurs
    val filet = TableTheme.espacements.filet

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(couleurs.fond)
            .drawBehind { drawLine(couleurs.filet, Offset.Zero, Offset(size.width, 0f), filet.toPx()) }
            .windowInsetsPadding(WindowInsets.navigationBars)
            .selectableGroup(),
    ) {
        elements.forEachIndexed { index, element ->
            Onglet(
                element = element,
                actif = index == selection,
                onClick = { onSelection(index) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun Onglet(element: ElementBarre, actif: Boolean, onClick: () -> Unit, modifier: Modifier) {
    val couleurs = TableTheme.couleurs

    Column(
        modifier = modifier
            .heightIn(min = HauteurOnglet)
            .selectable(selected = actif, role = Role.Tab, onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(TableTheme.espacements.s),
    ) {
        // Repère provisoire, il saute d'un onglet à l'autre à l'étape suivante
        Box(
            Modifier
                .size(width = 22.dp, height = 3.dp)
                .background(if (actif) couleurs.action else Color.Transparent, TableTheme.formes.badge),
        )
        Text(
            element.libelle,
            modifier = Modifier.padding(bottom = TableTheme.espacements.s),
            style = TableTheme.typo.onglet,
            fontWeight = if (actif) FontWeight.Bold else FontWeight.Medium,
            color = if (actif) couleurs.texte else couleurs.texteSecondaire,
        )
    }
}

private val HauteurOnglet = 56.dp

@ThemePreviews
@Composable
private fun ApercuBarreNavigation() = Planche {
    var selection by remember { mutableIntStateOf(0) }
    BarreNavigation(
        elements = listOf("Carte", "Explorer", "Accords", "Courses", "Favoris").map(::ElementBarre),
        selection = selection,
        onSelection = { selection = it },
    )
}
