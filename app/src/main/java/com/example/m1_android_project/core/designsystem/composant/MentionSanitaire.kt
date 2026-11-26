package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.Planche
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import com.example.m1_android_project.core.designsystem.theme.ThemePreviews

/** obligatoire sur chaque écran qui montre de l'alcool */
@Composable
fun MentionSanitaire(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(R.string.mention_sanitaire),
        modifier = modifier.fillMaxWidth(),
        color = TableTheme.couleurs.texteSecondaire,
        style = TableTheme.typo.secondaire,
        textAlign = TextAlign.Center,
    )
}

@ThemePreviews
@Composable
private fun ApercuMentionSanitaire() = Planche {
    LigneNumerotee("Vermentino", "12,5 % vol.", numero = 2)
    MentionSanitaire()
}
