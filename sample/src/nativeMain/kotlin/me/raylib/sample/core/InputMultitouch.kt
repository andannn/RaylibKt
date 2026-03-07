package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.core.Colors.BLACK
import raylib.core.Colors.ORANGE
import raylib.core.ComponentRegistry
import raylib.core.Vector2
import raylib.core.remember

fun ComponentRegistry.inputMultitouch() {
    component("key") {
        var pointers = remember {
            emptyList<CValue<Vector2>>()
        }
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
