package com.example.m1_android_project.core.network

import com.example.m1_android_project.core.model.Ingredient
import com.example.m1_android_project.core.model.lireQuantite
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class IngredientDto(val nom: String? = null, val mesure: String? = null)

/** Range strIngredientN et strMeasureN dans « ingredients », 20 cases pour un plat, 15 pour un cocktail */
open class AvecIngredients<T : Any>(serializer: KSerializer<T>) : JsonTransformingSerializer<T>(serializer) {

    override fun transformDeserialize(element: JsonElement): JsonElement {
        val objet = element as? JsonObject ?: return element
        val ingredients = generateSequence(1) { it + 1 }
            .takeWhile { "strIngredient$it" in objet }
            .map { n ->
                JsonObject(
                    mapOf(
                        "nom" to objet.getValue("strIngredient$n"),
                        "mesure" to (objet["strMeasure$n"] ?: JsonNull),
                    ),
                )
            }
        return JsonObject(objet + ("ingredients" to JsonArray(ingredients.toList())))
    }
}

/** Les cases vides sont ignorées, une mesure vide garde l'ingrédient */
internal fun List<IngredientDto>.versIngredients(): List<Ingredient> =
    mapNotNull { (nom, mesure) ->
        nom.texte()?.let { Ingredient(it, lireQuantite(mesure.texte())) }
    }
