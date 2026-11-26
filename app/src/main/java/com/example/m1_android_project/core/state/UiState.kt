package com.example.m1_android_project.core.state

/** Disponibilité d'un contenu, indépendante de Compose et du stockage. */
sealed interface UiState<out T : Any> {
    data object Chargement : UiState<Nothing>

    /** L'erreur de rafraîchissement accompagne le contenu déjà disponible en cache. */
    data class Contenu<out T : Any>(
        val donnees: T,
        val erreur: AppError? = null,
    ) : UiState<T>

    /** Échec de chargement sans contenu disponible. */
    data class Erreur(val cause: AppError) : UiState<Nothing>
}
