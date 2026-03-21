package me.raylib.sample.shape

import kotlinx.cinterop.readValue
import platform.posix.atan2f
import platform.posix.cosf
import platform.posix.sinf
import io.github.andannn.raylib.foundation.Colors.BLACK
import io.github.andannn.raylib.foundation.Colors.BROWN
import io.github.andannn.raylib.foundation.Colors.DARKGREEN
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.foundation.Vector2Alloc
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.isCollisionWith
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.set
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.setValue

fun ComponentRegistry.followingEyes() {
    component("A") {
        val scleraLeftPosition by remember {
            Vector2Alloc(screenWidth / 2.0f - 100.0f, screenHeight / 2.0f)
        }
        val scleraRightPosition by remember {
            Vector2Alloc(screenWidth / 2.0f + 100.0f, screenHeight / 2.0f)
        }
        val scleraRadius = 80f
        val irisLeftPosition by remember {
            Vector2Alloc(screenWidth / 2.0f - 100.0f, screenHeight / 2.0f)
        }
        val irisRightPosition by remember {
            Vector2Alloc(screenWidth / 2.0f + 100.0f, screenHeight / 2.0f)
        }
        val irisRadius = 24f

        var angle by remember {
            mutableStateOf(0.0f)
        }
        var dx by remember {
            mutableStateOf(0.0f)
        }
        var dy by remember {
            mutableStateOf(0.0f)
        }
        var dxx by remember {
            mutableStateOf(0.0f)
        }
        var dyy by remember {
            mutableStateOf(0.0f)
        }

        update {
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

        draw {
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
