package com.example.m1_android_project.core.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.TableTheme

/** Écran provisoire d'un onglet remplacé par le vrai dans son issue la liste sert à tester le défilement */
@Composable
fun EcranAVenir(
    titre: String,
    modifier: Modifier = Modifier,
    entete: @Composable () -> Unit = {},
) {
    val espacements = TableTheme.espacements

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(espacements.marge),
    ) {
        item {
            Column(
                modifier = Modifier.padding(bottom = espacements.l),
                verticalArrangement = Arrangement.spacedBy(espacements.m),
            ) {
                Text(titre, style = TableTheme.typo.titre)
                Text(
                    stringResource(R.string.ecran_a_venir),
                    style = TableTheme.typo.secondaire,
                    color = TableTheme.couleurs.texteSecondaire,
                )
                entete()
            }
        }
        items((1..LIGNES).toList()) { numero ->
            Column(Modifier.fillMaxWidth().heightIn(min = espacements.cibleTactile), verticalArrangement = Arrangement.Center) {
                Text(
                    stringResource(R.string.element_provisoire, numero),
                    modifier = Modifier.padding(vertical = espacements.m).align(Alignment.Start),
                    style = TableTheme.typo.libelle,
                )
                HorizontalDivider(thickness = espacements.filet, color = TableTheme.couleurs.filet)
            }
        }
    }
}

private const val LIGNES = 40
