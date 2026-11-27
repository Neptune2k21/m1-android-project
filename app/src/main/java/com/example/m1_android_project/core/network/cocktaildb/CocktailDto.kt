package com.example.m1_android_project.core.network.cocktaildb

import com.example.m1_android_project.core.network.AvecIngredients
import com.example.m1_android_project.core.network.IngredientDto
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonTransformingSerializer

@Serializable
data class ReponseBoissons(
    @Serializable(with = ListeBoissons::class) val drinks: List<BoissonDto> = emptyList(),
)

@Serializable
data class ReponseResumesBoissons(
    @Serializable(with = ListeResumes::class) val drinks: List<ResumeBoissonDto> = emptyList(),
)

@Serializable
data class ReponseVerres(val drinks: List<VerreDto>? = null)

@Serializable
data class BoissonDto(
    val idDrink: String? = null,
    val strDrink: String? = null,
    val strAlcoholic: String? = null,
    val strGlass: String? = null,
    val strInstructions: String? = null,
    val strDrinkThumb: String? = null,
    val ingredients: List<IngredientDto> = emptyList(),
)

@Serializable
data class ResumeBoissonDto(
    val idDrink: String? = null,
    val strDrink: String? = null,
    val strDrinkThumb: String? = null,
)

@Serializable
data class VerreDto(val strGlass: String? = null)

/** Sans résultat l'API répond null ou le texte « no data found » au lieu d'une liste */
open class ListeOuTexte<T>(element: KSerializer<T>) : JsonTransformingSerializer<List<T>>(ListSerializer(element)) {
    override fun transformDeserialize(element: JsonElement): JsonElement =
        if (element is JsonPrimitive) JsonArray(emptyList()) else element
}

object ListeBoissons : ListeOuTexte<BoissonDto>(AvecIngredients(BoissonDto.serializer()))

object ListeResumes : ListeOuTexte<ResumeBoissonDto>(ResumeBoissonDto.serializer())
