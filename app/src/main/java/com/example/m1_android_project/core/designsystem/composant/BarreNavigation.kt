package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.example.m1_android_project.core.designsystem.icone.IconeAccords
import com.example.m1_android_project.core.designsystem.icone.IconeCarte
import com.example.m1_android_project.core.designsystem.icone.IconeCourses
import com.example.m1_android_project.core.designsystem.icone.IconeExplorer
import com.example.m1_android_project.core.designsystem.icone.IconeFavoris
import com.example.m1_android_project.core.designsystem.icone.cles
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** Un onglet de la barre. badge -> nombre affiché sur l'icône, rien si nul ou à zéro */
data class ElementBarre(
    val libelle: String,
    val icone: @Composable (actif: Boolean) -> Unit,
    val badge: Int? = null,
)

/** Barre du bas. Un grain agrume saute jusqu'à l'onglet choisi */
@Composable
fun BarreNavigation(
    elements: List<ElementBarre>,
    selection: Int,
    onSelection: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val couleurs = TableTheme.couleurs
    val filet = TableTheme.espacements.filet
    val saut = remember { Animatable(1f) }
    var depart by remember { mutableIntStateOf(selection) }
    var arrivee by remember { mutableIntStateOf(selection) }

    LaunchedEffect(selection) {
        if (selection == arrivee) return@LaunchedEffect
        depart = arrivee
        arrivee = selection
        saut.snapTo(0f)
        saut.animateTo(1f, tween(DureeSaut, easing = LinearEasing))
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(couleurs.fond)
            .drawWithContent {
                drawContent()
                drawLine(couleurs.filet, Offset.Zero, Offset(size.width, 0f), filet.toPx())
                // Vol en arc
                val p = saut.value
                val vol = (p / .82f).coerceAtMost(1f)
                val largeurOnglet = size.width / elements.size
                val x = lerp(largeurOnglet * (depart + .5f), largeurOnglet * (arrivee + .5f), FastOutSlowInEasing.transform(vol))
                val y = HauteurGrain.toPx() - HauteurSaut.toPx() * 4 * vol * (1 - vol)
                val largeur = cles(p, 0f to 1f, .48f to 1.15f, .82f to 1.35f, 1f to 1f)
                val hauteur = cles(p, 0f to 1f, .48f to 1.15f, .82f to .65f, 1f to 1f)
                val rayon = TailleGrain.toPx() / 2
                scale(largeur, hauteur, pivot = Offset(x, y + rayon)) {
                    drawCircle(couleurs.accent, rayon, Offset(x, y))
                }
            }
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
            // L'icône qui s'anime suffit comme retour, sans rectangle gris au toucher
            .selectable(selected = actif, interactionSource = null, indication = null, role = Role.Tab, onClick = onClick)
            .padding(top = 14.dp, bottom = TableTheme.espacements.s),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
        Box {
            element.icone(actif)
            Badge(element.badge, Modifier.align(Alignment.TopEnd).offset(x = 9.dp, y = (-6).dp))
        }
        Text(
            element.libelle,
            style = TableTheme.typo.onglet,
            fontWeight = if (actif) FontWeight.Bold else FontWeight.Medium,
            color = if (actif) couleurs.texte else couleurs.texteSecondaire,
        )
    }
}

@Composable
private fun Badge(nombre: Int?, modifier: Modifier) {
    val visible = nombre != null && nombre > 0
    // Garde le dernier nombre pendant que la pastille disparaît
    var affiche by remember { mutableIntStateOf(0) }
    if (nombre != null && nombre > 0) affiche = nombre

    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = scaleIn() + fadeIn(),
        exit = scaleOut() + fadeOut(),
    ) {
        Text(
            if (affiche > 99) "99+" else "$affiche",
            modifier = Modifier
                .defaultMinSize(minWidth = 16.dp, minHeight = 16.dp)
                .background(TableTheme.couleurs.accent, TableTheme.formes.badge)
                .padding(horizontal = 4.dp),
            style = TableTheme.typo.badge.copy(fontSize = 10.sp, lineHeight = 16.sp),
            color = TableTheme.couleurs.surAccent,
            textAlign = TextAlign.Center,
        )
    }
}

private const val DureeSaut = 460
private val HauteurOnglet = 56.dp
private val TailleGrain = 7.dp
private val HauteurGrain = 3.dp
private val HauteurSaut = 15.dp

@ThemePreviews
@Composable
private fun ApercuBarreNavigation() = Planche {
    var selection by remember { mutableIntStateOf(0) }
    BarreNavigation(
        elements = listOf(
            ElementBarre("Carte", { IconeCarte(it) }),
            ElementBarre("Explorer", { IconeExplorer(it) }),
            ElementBarre("Accords", { IconeAccords(it) }),
            ElementBarre("Courses", { IconeCourses(it) }, badge = 3),
            ElementBarre("Favoris", { IconeFavoris(it) }),
        ),
        selection = selection,
        onSelection = { selection = it },
    )
}
