package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews
import com.example.m1_android_project.core.network.Sources

/** Pays à toucher pour voir sa cuisine, ou filtre à sélectionner */
@Composable
fun PucePays(
    nom: String,
    drapeau: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectionne: Boolean = false,
) {
    val couleurs = TableTheme.couleurs
    val espacements = TableTheme.espacements
    val forme = TableTheme.formes.puce
    // Sélectionnée, la puce passe en encre pleine
    val fond by animateColorAsState(if (selectionne) couleurs.texte else couleurs.fond, label = "fond")
    val encre by animateColorAsState(if (selectionne) couleurs.fond else couleurs.texte, label = "encre")

    Row(
        modifier = modifier
            .heightIn(min = espacements.cibleTactile)
            .clip(forme)
            .background(fond)
            .border(espacements.filet, couleurs.filet, forme)
            .selectable(selected = selectionne, role = Role.Button, onClick = onClick)
            .padding(start = espacements.s, end = espacements.m),
        horizontalArrangement = Arrangement.spacedBy(espacements.s),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Drapeau(drapeau)
        Text(nom, color = encre, style = TableTheme.typo.libelle)
    }
}

/** Uni tant que l'image n'est pas chargée */
@Composable
private fun Drapeau(url: String?) {
    val forme = TableTheme.formes.case

    AsyncImage(
        model = url,
        contentDescription = null,
        modifier = Modifier
            .size(TailleDrapeau)
            .clip(forme)
            .background(TableTheme.couleurs.surface)
            .border(TableTheme.espacements.filet, TableTheme.couleurs.filet, forme),
        // Le PNG est en 3:2, on garde le centre
        contentScale = ContentScale.Crop,
    )
}

private val TailleDrapeau = 28.dp

@ThemePreviews
@Composable
private fun ApercuPucePays() = Planche {
    var choisi by remember { mutableStateOf("jp") }

    Row(horizontalArrangement = Arrangement.spacedBy(TableTheme.espacements.s)) {
        listOf("it" to "Italie", "ma" to "Maroc", "jp" to "Japon").forEach { (code, nom) ->
            PucePays(nom, Sources.drapeau(code), onClick = { choisi = code }, selectionne = code == choisi)
        }
    }
}
