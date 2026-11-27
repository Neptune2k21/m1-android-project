package com.example.m1_android_project.carte

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.navigation.photoPartagee
import com.example.m1_android_project.core.ui.EcranAVenir

/** Carte du jour à construire */
@Composable
fun CarteEcran(onOuvrirFiche: (id: String) -> Unit, modifier: Modifier = Modifier) {
    EcranAVenir(titre = stringResource(R.string.onglet_carte), modifier = modifier) {
        // Seul moyen d'ouvrir une fiche tant que la carte n'existe pas
        Column(
            Modifier
                .fillMaxWidth()
                .clip(TableTheme.formes.carte)
                .background(TableTheme.couleurs.teinteAction)
                .clickable(role = Role.Button) { onOuvrirFiche(FICHE_EXEMPLE) },
        ) {
            // Tient la place de la photo du plat jusqu'à la #11
            Box(
                Modifier
                    .photoPartagee(FICHE_EXEMPLE)
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
                    .background(TableTheme.couleurs.teinteAccent),
            )
            Text(
                stringResource(R.string.fiche_exemple),
                modifier = Modifier.padding(TableTheme.espacements.l),
                style = TableTheme.typo.action,
                color = TableTheme.couleurs.action,
            )
        }
    }
}

private const val FICHE_EXEMPLE = "mealdb:52777"
