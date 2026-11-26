package com.example.m1_android_project.core.designsystem.composant

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

/** Coche tracée de 0 (rien) à 1 (complète) */
internal fun Modifier.coche(couleur: Color, trace: () -> Float = { 1f }) = drawWithCache {
    val mesure = PathMeasure().apply {
        setPath(
            Path().apply {
                moveTo(size.width * 0.28f, size.height * 0.52f)
                lineTo(size.width * 0.44f, size.height * 0.68f)
                lineTo(size.width * 0.73f, size.height * 0.36f)
            },
            false,
        )
    }
    val trait = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    val portion = Path()

    onDrawBehind {
        portion.reset()
        mesure.getSegment(0f, mesure.length * trace(), portion, true)
        drawPath(portion, couleur, style = trait)
    }
}
