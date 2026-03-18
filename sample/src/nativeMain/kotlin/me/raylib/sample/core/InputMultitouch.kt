package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import io.github.andannn.raylib.base.Colors.BLACK
import io.github.andannn.raylib.base.Colors.ORANGE
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember

fun ComponentRegistry.inputMultitouch() {
    component("key") {
        var pointers = remember {
            emptyList<CValue<Vector2>>()
        }
        update {
            pointers = touchPositions().toList()
        }
        draw {
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
