package com.example.m1_android_project.core.state

/** Causes métier du chargement, traduites en messages par l'interface. */
sealed interface AppError {
    data object HorsLigne : AppError
    data object DelaiDepasse : AppError
    data object Serveur : AppError
}
