package me.raylib.sample.shape

import io.github.andannn.raylib.foundation.Colors.BEIGE
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.BROWN
import io.github.andannn.raylib.foundation.Colors.DARKBLUE
import io.github.andannn.raylib.foundation.Colors.DARKGRAY
import io.github.andannn.raylib.foundation.Colors.GOLD
import io.github.andannn.raylib.foundation.Colors.GREEN
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.foundation.Colors.ORANGE
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.foundation.Colors.SKYBLUE
import io.github.andannn.raylib.foundation.Colors.VIOLET
import io.github.andannn.raylib.foundation.Colors.YELLOW
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Vector2
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue

fun ComponentRegistry.basicShapes() {
    component("key") {
        var rotation by remember {
            mutableStateOf(0.0f)
        }

        update {
            rotation += 0.2f;
        }
        draw {
            drawText("some basic shapes available on raylib", 20, 20, 20, DARKGRAY);
            drawCircle(screenWidth / 5, 120, 35f, DARKBLUE);
            drawCircleGradient(screenWidth / 5, 220, 60f, GREEN, SKYBLUE);
            drawCircleLines(screenWidth / 5, 340, 80f, DARKBLUE)
            drawEllipse(screenWidth / 5, 120, 25f, 20f, YELLOW);
            drawEllipseLines(screenWidth / 5, 120, 30f, 25f, YELLOW);

            drawRectangle(screenWidth / 4 * 2 - 60, 100, 120, 60, RED)
            drawRectangleGradientH(screenWidth / 4 * 2 - 90, 170, 180, 130, MAROON, GOLD);
            drawRectangleLines(screenWidth / 4 * 2 - 40, 320, 80, 60, ORANGE)

            drawTriangle(
                Vector2(screenWidth / 4.0f * 3.0f, 80.0f),
                Vector2(screenWidth / 4.0f * 3.0f - 60.0f, 150.0f),
                Vector2(screenWidth / 4.0f * 3.0f + 60.0f, 150.0f), VIOLET
            )

            drawTriangleLines(
                Vector2(screenWidth / 4.0f * 3.0f, 160.0f),
                Vector2(screenWidth / 4.0f * 3.0f - 20.0f, 230.0f),
                Vector2(screenWidth / 4.0f * 3.0f + 20.0f, 230.0f), DARKBLUE
            )

            drawPoly(Vector2(screenWidth / 4.0f * 3, 330f), 6, 80f, rotation, BROWN)
            drawPolyLines(Vector2(screenWidth / 4.0f * 3, 330f), 6, 90f, rotation, BROWN)
            drawPolyLines(Vector2(screenWidth / 4.0f * 3, 330f), 6, 85f, rotation, 6f, BEIGE)

            drawLine(18, 42, screenWidth - 18, 42, BLACK);
        }
    }
}
