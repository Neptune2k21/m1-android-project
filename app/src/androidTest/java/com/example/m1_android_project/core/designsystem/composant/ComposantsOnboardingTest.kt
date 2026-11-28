package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertHeightIsAtLeast
import androidx.compose.ui.test.assertIsNotSelected
import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.assertWidthIsAtLeast
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.unit.dp
import com.example.m1_android_project.R
import com.example.m1_android_project.core.designsystem.theme.TableTheme
import org.junit.Rule
import org.junit.Test

// Noms en camelCase : Android avant la 11 refuse les espaces dans les noms de méthode
class ComposantsOnboardingTest {

    @get:Rule
    val regle = createComposeRule()

    @Test
    fun uneTuileSeCocheEtSAnnonceCommeCaseACocher() {
        var cochee by mutableStateOf(false)
        regle.setContent {
            TableTheme {
                TuileChoix(
                    illustration = painterResource(R.drawable.illustration_regime_pescetarien),
                    titre = "Pescétarien",
                    precision = "Poisson, pas de viande",
                    cochee = cochee,
                    onCocheChange = { cochee = it },
                )
            }
        }

        regle.onNodeWithText("Pescétarien")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.Checkbox))
            .assertIsOff()
            .performClick()
            .assertIsOn()
            .assertHeightIsAtLeast(48.dp)
            .assertWidthIsAtLeast(48.dp)
    }

    @Test
    fun uneOptionSeChoisitCommeUnBoutonRadio() {
        var choix by mutableIntStateOf(0)
        regle.setContent {
            TableTheme {
                Column(Modifier.selectableGroup()) {
                    OptionIllustree(
                        illustration = painterResource(R.drawable.illustration_boissons_vins_et_cocktails),
                        titre = "Vins et cocktails",
                        texte = "Accords vin et cocktails",
                        choisie = choix == 0,
                        onChoix = { choix = 0 },
                    )
                    OptionIllustree(
                        illustration = painterResource(R.drawable.illustration_boissons_sans_alcool),
                        titre = "Uniquement sans alcool",
                        texte = "Mocktails, thés glacés, citronnades",
                        choisie = choix == 1,
                        onChoix = { choix = 1 },
                    )
                }
            }
        }

        regle.onNodeWithText("Uniquement sans alcool")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.Role, Role.RadioButton))
            .assertIsNotSelected()
            .performClick()
            .assertIsSelected()
        regle.onNodeWithText("Vins et cocktails").assertIsNotSelected()
    }

    @Test
    fun lesEtapesAnnoncentLEtapeActive() {
        regle.setContent {
            TableTheme { EtapesNumerotees(listOf("Régime", "Matériel", "Boissons"), active = 1) }
        }

        regle.onNodeWithContentDescription("Étape 2 sur 3, Matériel").assertExists()
    }
}
