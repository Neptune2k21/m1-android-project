package com.example.m1_android_project.core.network

import java.io.IOException
import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

/** Répond avec un JSON enregistré au lieu d'appeler le réseau, et garde l'URL demandée */
class FauxServeur : Interceptor {

    private var corps = ""
    private var code = 200
    private var panne: IOException? = null
    var derniereUrl: HttpUrl? = null
        private set

    val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(this).build()

    /** Fichier de src/test/resources/api, par exemple mealdb/lookup-52777.json */
    fun repondre(fichier: String) = repondreTexte(lire(fichier))

    fun repondreTexte(json: String, code: Int = 200) {
        corps = json
        this.code = code
        panne = null
    }

    fun couper(erreur: IOException) {
        panne = erreur
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        derniereUrl = chain.request().url
        panne?.let { throw it }
        return Response.Builder()
            .request(chain.request())
            .protocol(Protocol.HTTP_1_1)
            .code(code)
            .message("enregistré")
            .body(corps.toResponseBody("application/json".toMediaType()))
            .build()
    }

    private fun lire(fichier: String): String =
        checkNotNull(javaClass.getResource("/api/$fichier")) { "$fichier absent des ressources de test" }.readText()
}
