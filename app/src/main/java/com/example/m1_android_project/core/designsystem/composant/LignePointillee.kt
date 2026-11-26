package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.core.designsystem.theme.TableTheme

/** Libellé relié à sa valeur par des pointillés, posé sur un filet */
@Composable
internal fun LignePointillee(
    libelle: String,
    modifier: Modifier = Modifier,
    couleurLibelle: Color = TableTheme.couleurs.texteSecondaire,
    detail: String? = null,
    debut: @Composable (() -> Unit)? = null,
    fin: @Composable () -> Unit,
) {
    val espacements = TableTheme.espacements

    Column(modifier.semantics(mergeDescendants = true) {}) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = espacements.cibleTactile)
                .padding(vertical = espacements.m),
            horizontalArrangement = Arrangement.spacedBy(espacements.m),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            debut?.invoke()
            Column(Modifier.weight(1f).alignByBaseline()) {
                Row(horizontalArrangement = Arrangement.spacedBy(espacements.m)) {
                    Text(
                        text = libelle,
                        modifier = Modifier.alignByBaseline(),
                        color = couleurLibelle,
                        style = TableTheme.typo.libelle,
                    )
                    // Le bas des points se pose sur la ligne de base du libellé
                    Pointilles(Modifier.weight(1f).alignBy { it.measuredHeight })
                }
                if (detail != null) {
                    Text(
                        text = detail,
                        color = TableTheme.couleurs.texteSecondaire,
                        style = TableTheme.typo.secondaire,
                    )
                }
            }
            Box(Modifier.alignByBaseline()) { fin() }
        }
        HorizontalDivider(thickness = espacements.filet, color = TableTheme.couleurs.filet)
    }
}

@Composable
private fun Pointilles(modifier: Modifier = Modifier) {
    val couleur = TableTheme.couleurs.texteSecondaire.copy(alpha = 0.3f)

    Canvas(modifier.height(2.dp)) {
        val rayon = size.height / 2
        val pas = 4.dp.toPx()
        var x = rayon
        while (x <= size.width - rayon) {
            drawCircle(couleur, rayon, Offset(x, rayon))
            x += pas
        }
    }
}
