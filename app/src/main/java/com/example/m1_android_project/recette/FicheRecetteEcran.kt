package com.example.m1_android_project.recette

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.TableTheme

/** Fiche recette à construire  */
@Composable
fun FicheRecetteEcran(id: String, onRetour: () -> Unit, modifier: Modifier = Modifier) {
    val espacements = TableTheme.espacements

    Column(
        modifier = modifier.fillMaxSize().padding(espacements.marge),
        verticalArrangement = Arrangement.spacedBy(espacements.l),
    ) {
        Text(
            stringResource(R.string.retour),
            modifier = Modifier
                .heightIn(min = espacements.cibleTactile)
                .clip(TableTheme.formes.bouton)
                .clickable(role = Role.Button, onClick = onRetour)
                .padding(espacements.m),
            style = TableTheme.typo.action,
            color = TableTheme.couleurs.action,
        )
        Text(stringResource(R.string.fiche_titre, id), style = TableTheme.typo.titre)
        Text(
            stringResource(R.string.ecran_a_venir),
            style = TableTheme.typo.secondaire,
            color = TableTheme.couleurs.texteSecondaire,
        )
    }
}
