package me.raylib.sample.custom

import kotlinx.cinterop.readValue
import raylib.core.Colors.BLACK
import raylib.core.ComponentRegistry
import raylib.core.DisposableState
import raylib.core.Vector2Alloc
import raylib.core.randomColor
import raylib.core.randomValue
import raylib.core.mutableStateListOf
import raylib.core.nativeStateOf
import raylib.core.window

fun randomObjectGenerator() {
    window(
        title = "custom example - random object generator",
        width = 800,
        height = 450,
        initialBackGroundColor = BLACK
    ) {
        val stateList = mutableStateListOf<Int>()
        componentRegistry {
            component("random object generator") {
                var frameCount = 0
                var newId = 0
                onUpdate {
                    frameCount++
                    if (frameCount % 5 == 0) {
                        if (stateList.size <= 10000) {
                            stateList.addState(nativeStateOf { newId++ })
                        }
                    }
                }
            }

            stateList.forEach { state ->
                generatedObject(state)
            }
        }
    }
}

private fun ComponentRegistry.generatedObject(state: DisposableState<Int>) {
    component(state.value) {
        var frameCount = 0f
        val color = randomColor()
        val radius = randomValue(1, 10)
        val position by nativeStateOf {
            Vector2Alloc(
                randomValue(0, screenWidth).toFloat(),
                randomValue(0, screenHeight).toFloat()
            )
        }
        onUpdate {
            frameCount++

            if (frameCount > 60) {
                // make itself disappear
                state.dispose()
            }
        }

        onDraw {
            drawCircle(position.readValue(), radius.toFloat(), color)
        }
    }
}