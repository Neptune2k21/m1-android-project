package com.example.m1_android_project.core.network

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class NettoyageTest {

    @Test
    fun `retire les etiquettes seules sur leur ligne`() {
        assertEquals(
            listOf("Preheat the oven.", "Bake."),
            decouperEtapes("STEP 1\r\nPreheat the oven.\r\n\r\nstep 2\r\nBake.\r\n3."),
        )
    }

    @Test
    fun `retire le numero en tete sans toucher aux quantites`() {
        assertEquals(
            listOf("Mix the flour.", "Add 1.5 cups of milk.", "Rest", "Fry", "2 eggs, beaten"),
            decouperEtapes("1. Mix the flour.\n2) Add 1.5 cups of milk.\nSTEP 3 - Rest\n0.\tFry\n2 eggs, beaten"),
        )
    }

    @Test
    fun `retire les puces et les lignes sans texte`() {
        assertEquals(
            listOf("To store: keep cold.", "Serve."),
            decouperEtapes("▢\n*To store: keep cold.\n\u200B\n- Serve."),
        )
        assertEquals(emptyList<String>(), decouperEtapes(null))
    }

    @Test
    fun `prefixe l'id par sa source`() {
        assertEquals("mealdb:52777", identifiant("mealdb", " 52777 "))
        assertNull(identifiant("mealdb", "a:b"))
        assertNull(identifiant("mealdb", " "))
        assertNull(identifiant("mealdb", null))
    }
}
