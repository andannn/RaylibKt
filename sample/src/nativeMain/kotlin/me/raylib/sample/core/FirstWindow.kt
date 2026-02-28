package me.raylib.sample.core

import platform.posix.pthread_self
import raylib.core.Colors
import raylib.core.Colors.LIGHTGRAY
import raylib.core.window
import kotlin.io.println

internal fun firstWindow() {
    window(
        title = "raylib [core] example - basic window",
        initialFps = 60,
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("key") {
                provideHandlers {
                    suspendingScope {
                        awaitUpdateEventScope {
                            while (true) {
                                awaitUpdateEvent()
                                println("Native Thread ID: ${pthread_self()}")
                            }
                        }
                    }
                    onDraw {
                        drawText("Congrats! You created your first window!", 190, 200, 20, LIGHTGRAY);
                    }
                }
            }
        }
    }
}