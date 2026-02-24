package me.raylib.sample.shape

import kotlinx.cinterop.readValue
import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.BLUE
import raylib.core.Colors.GOLD
import raylib.core.Colors.LIME
import raylib.core.Colors.RED
import raylib.core.KeyboardKey
import raylib.core.RectangleAlloc
import raylib.core.getCollisionRec
import raylib.core.isCollisionWith
import raylib.core.set
import raylib.core.window
import raylib.interop.MeasureText

fun collisionArea() {
    window(
        title = "raylib [shapes] example - collision area",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("K") {
                val boxA = RectangleAlloc(10f, screenHeight / 2.0f - 50, 200f, 100f)
                var boxASpeedX = 4
                val boxB = RectangleAlloc(screenWidth / 2.0f - 30, screenHeight / 2.0f - 30, 60f, 60f)
                val boxCollision = RectangleAlloc()
                val screenUpperLimit = 40
                var pause = false
                var collision = false

                provideHandlers {
                    onUpdate {
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
                        if (collision){
                            boxCollision.set(boxA.readValue().getCollisionRec(boxB.readValue()))
                        }
                        if (KeyboardKey.KEY_SPACE.isPressed()) pause = !pause;
                    }

                    onDraw {
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
        }
    }
}