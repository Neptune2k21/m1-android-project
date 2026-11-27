package com.example.m1_android_project.core.model

/** Catégorie de plat, nom tel que l'attend le filtre (Beef, Dessert…) */
data class Categorie(
    val nom: String,
    val photoUrl: String? = null,
)
