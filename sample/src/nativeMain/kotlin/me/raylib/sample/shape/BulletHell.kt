// TODO: Bullet Hell
package me.raylib.sample.shape

import raylib.core.Color
import raylib.core.Colors
import raylib.core.Vector2
import raylib.core.window

class Bullet(
    position: Vector2,
    acceleration: Vector2,
    color: Color,
    disabled: Boolean = false,
) {

}

fun bulletHell() {
    window(
        title = "raylib [shapes] example - bouncing ball",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        componentRegistry {
            component("k") {
                val bullets = mutableListOf<Bullet>()
                val bulletDisabledCount = 0 // Used to calculate how many bullets are on screen
                val bulletRadius = 10
                val bulletSpeed = 3.0f
                val bulletRows = 6

                provideHandlers {

                }
            }
        }
    }
}