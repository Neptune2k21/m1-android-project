package com.example.m1_android_project.core.network

import org.junit.Assert.assertEquals
import org.junit.Test

class SourcesTest {

    @Test
    fun `encode le nom de l'ingredient dans l'URL`() {
        assertEquals(
            "https://www.themealdb.com/images/ingredients/Cherry%20Tomatoes-small.png",
            Sources.imageIngredient(" Cherry Tomatoes "),
        )
    }

    @Test
    fun `met le code pays en minuscules`() {
        assertEquals("https://flags.restcountries.com/v5/w160/ma.png", Sources.drapeau("MA"))
    }
}
