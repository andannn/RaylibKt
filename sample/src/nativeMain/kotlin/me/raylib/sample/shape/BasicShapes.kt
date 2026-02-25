package me.raylib.sample.shape

import raylib.core.Colors
import raylib.core.Colors.BEIGE
import raylib.core.Colors.BLACK
import raylib.core.Colors.BROWN
import raylib.core.Colors.DARKBLUE
import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.GOLD
import raylib.core.Colors.GREEN
import raylib.core.Colors.MAROON
import raylib.core.Colors.ORANGE
import raylib.core.Colors.RED
import raylib.core.Colors.SKYBLUE
import raylib.core.Colors.VIOLET
import raylib.core.Colors.YELLOW
import raylib.core.Vector2
import raylib.core.window

fun basicShapes() {
    window(
        title = "raylib [shapes] example - basic shapes",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("key") {
                var rotation = 0.0f

                provideHandlers {
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
        }
    }
}