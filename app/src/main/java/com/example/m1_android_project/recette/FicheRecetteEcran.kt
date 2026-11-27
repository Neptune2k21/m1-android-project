package com.example.m1_android_project.recette

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.navigation.photoPartagee

/** Fiche recette à construire  */
@Composable
fun FicheRecetteEcran(id: String, onRetour: () -> Unit, modifier: Modifier = Modifier) {
    val espacements = TableTheme.espacements

    // Fond plein : pendant le retour prédictif la fiche rétrécit au-dessus de la carte
    Column(modifier.fillMaxSize().background(TableTheme.couleurs.fond)) {
        Box {
            Box(
                Modifier
                    .photoPartagee(id)
                    .fillMaxWidth()
                    .height(HauteurPhoto)
                    .background(TableTheme.couleurs.teinteAccent),
            )
            Text(
                stringResource(R.string.retour),
                modifier = Modifier
                    .padding(espacements.m)
                    .heightIn(min = espacements.cibleTactile)
                    .clip(TableTheme.formes.bouton)
                    .background(TableTheme.couleurs.fond)
                    .clickable(role = Role.Button, onClick = onRetour)
                    .padding(horizontal = espacements.l, vertical = espacements.m),
                style = TableTheme.typo.action,
                color = TableTheme.couleurs.action,
            )
        }
        Column(
            modifier = Modifier.padding(espacements.marge),
            verticalArrangement = Arrangement.spacedBy(espacements.m),
        ) {
            Text(stringResource(R.string.fiche_titre, id), style = TableTheme.typo.titre)
            Text(
                stringResource(R.string.ecran_a_venir),
                style = TableTheme.typo.secondaire,
                color = TableTheme.couleurs.texteSecondaire,
            )
        }
    }
}

private val HauteurPhoto = 280.dp
