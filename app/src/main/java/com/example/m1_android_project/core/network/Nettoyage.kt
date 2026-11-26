package com.example.m1_android_project.core.network

import com.example.m1_android_project.core.model.normaliserEspaces

/** null pour une chaîne vide, espaces doubles retirés */
internal fun String?.texte(): String? = this?.let(::normaliserEspaces)?.ifEmpty { null }

/** mealdb:52777, ou null si l'id casserait ce format */
internal fun identifiant(source: String, brut: String?): String? =
    brut.texte()?.takeIf { id -> id.none { it == ':' || it.isWhitespace() } }?.let { "$source:$it" }

private val retourLigne = Regex("\\r\\n|\\r|\\n")
private val puce = Regex("^[*•▢-]+\\s*")
// « STEP 1 », « 2. » seuls sur leur ligne
private val etiquette = Regex("^(?:step\\s*)?\\d+[.):]?$", RegexOption.IGNORE_CASE)
// « 1. Faire… », « 2) Faire… », « STEP 3 - Faire… »
private val numero = Regex("^(?:step\\s*)?\\d+\\s*[.):-]\\s+|^step\\s*\\d+\\s+", RegexOption.IGNORE_CASE)

/** Une étape par ligne, sans numéro ni puce : l'écran numérote lui-même */
internal fun decouperEtapes(instructions: String?): List<String> =
    instructions.orEmpty()
        .split(retourLigne)
        .map { it.trim().replace(puce, "") }
        .filterNot { etiquette.matches(it) }
        .map { it.replace(numero, "").trim() }
        .filter { ligne -> ligne.any(Char::isLetterOrDigit) }
