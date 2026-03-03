package me.raylib.sample.shape

import kotlinx.cinterop.readValue
import platform.posix.atan2f
import platform.posix.cosf
import platform.posix.sinf
import raylib.core.Colors.BLACK
import raylib.core.Colors.BROWN
import raylib.core.Colors.DARKGREEN
import raylib.core.Colors.LIGHTGRAY
import raylib.core.ComponentRegistry
import raylib.core.Vector2Alloc
import raylib.core.isCollisionWith
import raylib.core.set
import raylib.core.nativeStateOf

fun ComponentRegistry.followingEyes() {
    component("A") {
        val scleraLeftPosition by nativeStateOf {
            Vector2Alloc(screenWidth / 2.0f - 100.0f, screenHeight / 2.0f)
        }
        val scleraRightPosition by nativeStateOf { Vector2Alloc(screenWidth / 2.0f + 100.0f, screenHeight / 2.0f) }
        val scleraRadius = 80f
        val irisLeftPosition by nativeStateOf { Vector2Alloc(screenWidth / 2.0f - 100.0f, screenHeight / 2.0f) }
        val irisRightPosition by nativeStateOf { Vector2Alloc(screenWidth / 2.0f + 100.0f, screenHeight / 2.0f) }
        val irisRadius = 24f

        var angle = 0.0f
        var dx = 0.0f
        var dy = 0.0f
        var dxx = 0.0f
        var dyy = 0.0f

        onUpdate {
            irisLeftPosition.set(mousePosition)
            irisRightPosition.set(mousePosition)

            if (!irisLeftPosition.isCollisionWith(
                    scleraLeftPosition.readValue(),
                    scleraRadius - irisRadius
                )
            ) {
                dx = irisLeftPosition.x - scleraLeftPosition.x;
                dy = irisLeftPosition.y - scleraLeftPosition.y;

                angle = atan2f(dy, dx);

                dxx = (scleraRadius - irisRadius) * cosf(angle);
                dyy = (scleraRadius - irisRadius) * sinf(angle);

                irisLeftPosition.x = scleraLeftPosition.x + dxx;
                irisLeftPosition.y = scleraLeftPosition.y + dyy;
            }

            if (!irisRightPosition.isCollisionWith(
                    scleraRightPosition.readValue(),
                    scleraRadius - irisRadius
                )
            ) {
                dx = irisRightPosition.x - scleraRightPosition.x;
                dy = irisRightPosition.y - scleraRightPosition.y;

                angle = atan2f(dy, dx);

                dxx = (scleraRadius - irisRadius) * cosf(angle);
                dyy = (scleraRadius - irisRadius) * sinf(angle);

                irisRightPosition.x = scleraRightPosition.x + dxx;
                irisRightPosition.y = scleraRightPosition.y + dyy;

            }
        }

        onDraw {
            drawCircle(scleraLeftPosition.readValue(), scleraRadius, LIGHTGRAY);
            drawCircle(irisLeftPosition.readValue(), irisRadius, BROWN);
            drawCircle(irisLeftPosition.readValue(), 10f, BLACK);

            drawCircle(scleraRightPosition.readValue(), scleraRadius, LIGHTGRAY);
            drawCircle(irisRightPosition.readValue(), irisRadius, DARKGREEN);
            drawCircle(irisRightPosition.readValue(), 10f, BLACK);

            drawFPS(10, 10);

        }
    }
}
