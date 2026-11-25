package com.example.m1_android_project.core.testing

import com.example.m1_android_project.core.model.Cocktail
import com.example.m1_android_project.core.model.CouleurVin
import com.example.m1_android_project.core.model.Ingredient
import com.example.m1_android_project.core.model.Majorite
import com.example.m1_android_project.core.model.PreferenceBoissons
import com.example.m1_android_project.core.model.Profil
import com.example.m1_android_project.core.model.Quantite
import com.example.m1_android_project.core.model.Recette
import com.example.m1_android_project.core.model.Vin
import com.example.m1_android_project.core.model.lireQuantite

/** Exemples locaux reproductibles. Chaque accès retourne une nouvelle valeur. */
object Fixtures {
    val recette: Recette
        get() = Recette(
            id = "mealdb:fixture-tomates",
            nom = "Tomates assaisonnées",
            ingredients = listOf(
                Ingredient("Tomates cerises", lireQuantite("250g")),
                Ingredient("Sel", Quantite.Libre("to taste")),
            ),
            etapes = listOf("Laver les tomates.", "Couper et assaisonner."),
        )

    val cocktailSansAlcool: Cocktail
        get() = Cocktail(
            id = "cocktaildb:fixture-citron",
            nom = "Citron pétillant",
            contientAlcool = false,
            ingredients = listOf(
                Ingredient("Citron", lireQuantite("½")),
                Ingredient("Eau gazeuse", lireQuantite("15 cl")),
            ),
            etapes = listOf("Presser le citron.", "Ajouter l'eau gazeuse."),
        )

    val vins: List<Vin>
        get() = listOf(
            Vin(
                id = "vin:merlot",
                nom = "Merlot rouge",
                cepage = "Merlot",
                couleur = CouleurVin.Rouge,
                profilGustatif = "Fruité et souple",
                temperatureServiceCelsius = 16,
                accords = listOf("Plats mijotés", "Légumes rôtis"),
            ),
            Vin(
                id = "vin:chardonnay",
                nom = "Chardonnay blanc",
                cepage = "Chardonnay",
                couleur = CouleurVin.Blanc,
                profilGustatif = "Sec et rond",
                temperatureServiceCelsius = 10,
                accords = listOf("Poisson", "Volaille"),
            ),
            Vin(
                id = "vin:grenache",
                nom = "Grenache rosé",
                cepage = "Grenache",
                couleur = CouleurVin.Rose,
                profilGustatif = "Léger et fruité",
                temperatureServiceCelsius = 10,
                accords = listOf("Salades", "Grillades"),
            ),
        )

    val profilMineur: Profil
        get() = Profil(majorite = Majorite.Mineur)

    val profilMajeurSansAlcool: Profil
        get() = Profil(majorite = Majorite.Majeur)

    val profilMajeurAvecAlcool: Profil
        get() = Profil(
            majorite = Majorite.Majeur,
            preferenceBoissons = PreferenceBoissons.VinsEtCocktails,
        )
}
