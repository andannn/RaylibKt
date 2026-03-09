package me.raylib.sample.shape

import io.github.andannn.raylib.base.Colors.BEIGE
import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.BROWN
import io.github.andannn.raylib.base.Colors.DARKBLUE
import io.github.andannn.raylib.base.Colors.DARKGRAY
import io.github.andannn.raylib.base.Colors.GOLD
import io.github.andannn.raylib.base.Colors.GREEN
import io.github.andannn.raylib.base.Colors.MAROON
import io.github.andannn.raylib.base.Colors.ORANGE
import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Colors.SKYBLUE
import io.github.andannn.raylib.base.Colors.VIOLET
import io.github.andannn.raylib.base.Colors.YELLOW
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.basicShapes() {
    component("key") {
        var rotation by remember {
            mutableStateOf(0.0f)
        }

        onUpdate {
            rotation += 0.2f;
        }
        onDraw {
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
