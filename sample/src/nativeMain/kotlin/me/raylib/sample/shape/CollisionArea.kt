package me.raylib.sample.shape

import kotlinx.cinterop.readValue
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.BLUE
import io.github.andannn.raylib.foundation.Colors.GOLD
import io.github.andannn.raylib.foundation.Colors.LIME
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.foundation.RectangleAlloc
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.getCollisionRec
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.isCollisionWith
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.set
import io.github.andannn.raylib.runtime.nativeStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.setValue
import raylib.interop.MeasureText

private const val screenUpperLimit = 40

fun ComponentRegistry.collisionArea() {
    component("K") {
        val boxA by remember {
            nativeStateOf { RectangleAlloc(10f, screenHeight / 2.0f - 50, 200f, 100f) }
        }
        var boxASpeedX by remember {
            mutableStateOf(4)
        }
        val boxB by remember {
            nativeStateOf { RectangleAlloc(screenWidth / 2.0f - 30, screenHeight / 2.0f - 30, 60f, 60f) }
        }
        val boxCollision by remember {
            nativeStateOf { RectangleAlloc() }
        }
        var pause by remember {
            mutableStateOf(false)
        }
        var collision by remember {
            mutableStateOf(false)
        }

        update {
            if (!pause) boxA.x += boxASpeedX
            if (((boxA.x + boxA.width) >= screenWidth) || (boxA.x <= 0)) boxASpeedX *= -1
            boxB.x = mouseX - boxB.width / 2;
            boxB.y = mouseY - boxB.height / 2;

            if ((boxB.x + boxB.width) >= screenWidth) {
                boxB.x = screenWidth - boxB.width;
            } else if (boxB.x <= 0) {
                boxB.x = 0f
            }

            if ((boxB.y + boxB.height) >= screenHeight) {
                boxB.y = screenHeight - boxB.height
            } else if (boxB.y <= screenUpperLimit) {
                boxB.y = screenUpperLimit.toFloat()
            }

            collision = boxA.readValue().isCollisionWith(boxB.readValue())
            if (collision) {
                boxCollision.set(boxA.readValue().getCollisionRec(boxB.readValue()))
            }
            if (KeyboardKey.KEY_SPACE.isPressed()) pause = !pause;
        }

        draw {
            drawRectangle(0, 0, screenWidth, screenUpperLimit, if (collision) RED else BLACK)
            drawRectangle(boxA.readValue(), GOLD)
            drawRectangle(boxB.readValue(), BLUE)
            if (collision) {
                // Draw collision area
                drawRectangle(boxCollision.readValue(), LIME)

                // Draw collision message
                drawText(
                    "COLLISION!",
                    screenWidth / 2 - MeasureText("COLLISION!", 20) / 2,
                    screenUpperLimit / 2 - 10,
                    20,
                    BLACK
                )

                // Draw collision area
                drawText(
                    "Collision Area: ${boxCollision.width * boxCollision.height}",
                    screenWidth / 2 - 100,
                    screenUpperLimit + 10,
                    20,
                    BLACK
                )
                drawFPS(10, 10);
            }
        }
    }
}
