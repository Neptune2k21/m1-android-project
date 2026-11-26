package com.example.m1_android_project.core.model

enum class CouleurVin { Rouge, Blanc, Rose, Effervescent }

data class Vin(
    override val id: String,
    override val nom: String,
    val cepage: String,
    val couleur: CouleurVin,
    val profilGustatif: String,
    val accords: List<String>,
    override val photoUrl: String? = null,
    val temperatureServiceCelsius: Int? = null,
) : Boisson {
    override val contientAlcool: Boolean = true

    init {
        verifierIdentite(id, nom)
        require(cepage.isNotBlank()) { "Le cépage doit être renseigné." }
    }
}
