package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors.BLACK
import raylib.core.Colors.DARKBLUE
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIME
import raylib.core.Colors.MAROON
import raylib.core.Colors.RAYWHITE
import raylib.core.ConfigFlags
import raylib.core.KeyboardKey
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.window
import raylib.interop.DrawText

fun windowFlags() {
    window(
        title = "raylib [core] example - window flags",
        width = 800,
        height = 450,
        initialBackGroundColor = BLACK
    ) {
        gameComponent {
            val ballPosition = alloc<Vector2> { x = screenWidth.div(2f); y = screenHeight.div(2f) }
            val ballSpeed = alloc<Vector2> { x = 5f; y = 4f }
            val ballRadius = 20
            var framesCounter = 0

            provideHandlers {
                onUpdate {
                    // modifies window size when scaling!
                    if (KeyboardKey.KEY_F.isPressed()) toggleFullScreen()

                    if (KeyboardKey.KEY_R.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_RESIZABLE) {
                            if (isEnabled()) clear() else set()
                        }
                    }

                    if (KeyboardKey.KEY_D.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_UNDECORATED) {
                            if (isEnabled()) clear() else set()
                        }
                    }

                    if (KeyboardKey.KEY_H.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_HIDDEN) {
                            if (!isEnabled()) set()
                            framesCounter = 0;
                        }
                    }
                    if (ConfigFlags.FLAG_WINDOW_HIDDEN.isEnabled()) {
                        framesCounter++
                        if (framesCounter >= 240) ConfigFlags.FLAG_WINDOW_HIDDEN.clear()
                    }

                    if (KeyboardKey.KEY_N.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_MINIMIZED) {
                            if (!isEnabled()) minimizeWindow()
                            framesCounter = 0;
                        }
                    }
                    if (ConfigFlags.FLAG_WINDOW_MINIMIZED.isEnabled()) {
                        framesCounter++
                        if (framesCounter >= 240) {
                            restoreWindow()
                            framesCounter = 0
                        }
                    }

                    if (KeyboardKey.KEY_M.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_MAXIMIZED) {
                            if (isEnabled()) restoreWindow() else maximizeWindow()
                        }
                    }

                    if (KeyboardKey.KEY_U.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_UNFOCUSED) {
                            if (isEnabled()) clear() else set()
                        }
                    }

                    if (KeyboardKey.KEY_T.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_TOPMOST) {
                            if (isEnabled()) clear() else set()
                        }
                    }
                    if (KeyboardKey.KEY_A.isReleased()) {
                        with(ConfigFlags.FLAG_WINDOW_ALWAYS_RUN) {
                            if (isEnabled()) clear() else set()
                        }
                    }
                    if (KeyboardKey.KEY_V.isReleased()) {
                        with(ConfigFlags.FLAG_VSYNC_HINT) {
                            if (isEnabled()) clear() else set()
                        }
                    }
                    if (KeyboardKey.KEY_B.isReleased()) {
                        toggleBorderlessWindowed()
                    }
                    ballPosition.x += ballSpeed.x
                    ballPosition.y += ballSpeed.y

                    // Check walls collision for bouncing
                    if ((ballPosition.x >= (screenWidth - ballRadius)) || (ballPosition.x <= ballRadius)) ballSpeed.x *= -1.0f
                    if ((ballPosition.y >= (screenHeight - ballRadius)) || (ballPosition.y <= ballRadius)) ballSpeed.y *= -0.95f
                }

                onDraw {
                    drawCircle(ballPosition.readValue(), ballRadius.toFloat(), MAROON)
                    drawRectangleLines(
                        Rectangle(width = screenWidth.toFloat(), height = screenHeight.toFloat()), 4f, RAYWHITE
                    )
                    drawCircle(mousePosition, 10f, DARKBLUE);
                    drawFPS(10, 10)
                }
            }
        }

        gameComponent {
            provideHandlers {
                onDraw {
                    // Draw window state info
                    DrawText("Following flags can be set after window creation:", 10, 60, 10, GRAY)

                    if (ConfigFlags.FLAG_FULLSCREEN_MODE.isEnabled()) DrawText(
                        "[F] FLAG_FULLSCREEN_MODE: on",
                        10,
                        80,
                        10,
                        LIME
                    )
                    else DrawText("[F] FLAG_FULLSCREEN_MODE: off", 10, 80, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_RESIZABLE.isEnabled()) DrawText(
                        "[R] FLAG_WINDOW_RESIZABLE: on",
                        10,
                        100,
                        10,
                        LIME
                    )
                    else DrawText("[R] FLAG_WINDOW_RESIZABLE: off", 10, 100, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_UNDECORATED.isEnabled()) DrawText(
                        "[D] FLAG_WINDOW_UNDECORATED: on",
                        10,
                        120,
                        10,
                        LIME
                    )
                    else DrawText("[D] FLAG_WINDOW_UNDECORATED: off", 10, 120, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_HIDDEN.isEnabled()) DrawText(
                        "[H] FLAG_WINDOW_HIDDEN: on",
                        10,
                        140,
                        10,
                        LIME
                    )
                    else DrawText("[H] FLAG_WINDOW_HIDDEN: off (hides for 3 seconds)", 10, 140, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_MINIMIZED.isEnabled()) DrawText(
                        "[N] FLAG_WINDOW_MINIMIZED: on",
                        10,
                        160,
                        10,
                        LIME
                    )
                    else DrawText("[N] FLAG_WINDOW_MINIMIZED: off (restores after 3 seconds)", 10, 160, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_MAXIMIZED.isEnabled()) DrawText(
                        "[M] FLAG_WINDOW_MAXIMIZED: on",
                        10,
                        180,
                        10,
                        LIME
                    )
                    else DrawText("[M] FLAG_WINDOW_MAXIMIZED: off", 10, 180, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_UNFOCUSED.isEnabled()) DrawText(
                        "[G] FLAG_WINDOW_UNFOCUSED: on",
                        10,
                        200,
                        10,
                        LIME
                    )
                    else DrawText("[U] FLAG_WINDOW_UNFOCUSED: off", 10, 200, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_TOPMOST.isEnabled()) DrawText(
                        "[T] FLAG_WINDOW_TOPMOST: on",
                        10,
                        220,
                        10,
                        LIME
                    )
                    else DrawText("[T] FLAG_WINDOW_TOPMOST: off", 10, 220, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_ALWAYS_RUN.isEnabled()) DrawText(
                        "[A] FLAG_WINDOW_ALWAYS_RUN: on",
                        10,
                        240,
                        10,
                        LIME
                    )
                    else DrawText("[A] FLAG_WINDOW_ALWAYS_RUN: off", 10, 240, 10, MAROON)

                    if (ConfigFlags.FLAG_VSYNC_HINT.isEnabled()) DrawText("[V] FLAG_VSYNC_HINT: on", 10, 260, 10, LIME)
                    else DrawText("[V] FLAG_VSYNC_HINT: off", 10, 260, 10, MAROON)

                    if (ConfigFlags.FLAG_BORDERLESS_WINDOWED_MODE.isEnabled()) DrawText(
                        "[B] FLAG_BORDERLESS_WINDOWED_MODE: on",
                        10,
                        280,
                        10,
                        LIME
                    )
                    else DrawText("[B] FLAG_BORDERLESS_WINDOWED_MODE: off", 10, 280, 10, MAROON)

                    DrawText("Following flags can only be set before window creation:", 10, 320, 10, GRAY)

                    if (ConfigFlags.FLAG_WINDOW_HIGHDPI.isEnabled()) DrawText("FLAG_WINDOW_HIGHDPI: on", 10, 340, 10, LIME)
                    else DrawText("FLAG_WINDOW_HIGHDPI: off", 10, 340, 10, MAROON)

                    if (ConfigFlags.FLAG_WINDOW_TRANSPARENT.isEnabled()) DrawText(
                        "FLAG_WINDOW_TRANSPARENT: on",
                        10,
                        360,
                        10,
                        LIME
                    )
                    else DrawText("FLAG_WINDOW_TRANSPARENT: off", 10, 360, 10, MAROON)

                    if (ConfigFlags.FLAG_MSAA_4X_HINT.isEnabled()) DrawText("FLAG_MSAA_4X_HINT: on", 10, 380, 10, LIME)
                    else DrawText("FLAG_MSAA_4X_HINT: off", 10, 380, 10, MAROON)
                }
            }
        }
    }
}