package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.ORANGE
import raylib.core.Vector2
import raylib.core.window

fun inputMultitouch() {
    window(
        title = "raylib [core] example - input multitouch",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("key") {
                var pointers = emptyList<CValue<Vector2>>()
                provideHandlers {
                    onUpdate {
                        pointers = touchPositions().toList()
                    }
                    onDraw {
                        pointers.forEachIndexed { index, position ->
                            position.useContents {
                                if (x > 0 && y > 0) {
                                    drawCircle(position, 34f, ORANGE)
                                    drawText(
                                        index.toString(),
                                        (x - 10).toInt(),
                                        (y - 70).toInt(),
                                        40,
                                        BLACK
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
