package me.raylib.sample.custom

import raylib.core.Colors.RED
import raylib.core.ComponentRegistry
import raylib.core.Rectangle
import raylib.core.getValue
import raylib.core.matrixTranslate
import raylib.core.mutableStateOf
import raylib.core.rlMatrix
import raylib.core.setValue

fun ComponentRegistry.matrixTest() {
    component("matrixTest") {
        var angle by remember {
            mutableStateOf(0f)
        }
        onUpdate { deltaTime ->
            angle += 1f
        }
        onDraw {
            rlMatrix {
                translate(0f, 25 * 50f, 0f)
                rotate(90f, 1f)
                drawGrid(100, 50f)
            }
            rlMatrix {
                multMatrix(matrixTranslate(25f, 25f))
//                multMatrix(matrixScale(2f, 2f))
//                multMatrix(matrixRotateZ(angle))
//                rotate(angle, z = 1f)
                drawRectangle(Rectangle(-25f, -25f, 50f, 50f), color = RED)
            }
        }
    }
}