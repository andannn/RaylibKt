package me.raylib.sample.custom

import kotlinx.cinterop.CValue
import raylib.core.Colors.BLACK
import raylib.core.DisposableState
import raylib.core.ComponentFactory
import raylib.core.Vector2
import raylib.core.disposableState
import raylib.core.randomColor
import raylib.core.randomValue
import raylib.core.stateListOf
import raylib.core.window

fun randomObjectGenerator() {
    window(
        title = "custom example - random object generator",
        width = 800,
        height = 450,
        initialBackGroundColor = BLACK
    ) {
        val stateList = stateListOf<Int>()
        componentRegistry {
            component("random object generator") {
                var frameCount = 0
                var newId = 0
                provideHandlers {
                    onUpdate {
                        frameCount++
                        if (frameCount % 5 == 0) {
                            if (stateList.size <= 10000) {
                                stateList.addState(disposableState { newId++ })
                            }
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

private fun ComponentFactory.generatedObject(state: DisposableState<Int>) {
    component(state.value) {
        var frameCount = 0f
        val color = randomColor()
        val radius = randomValue(1, 10)
        var position: CValue<Vector2>? = null
        provideHandlers {
            onUpdate {
                frameCount++
                if (position == null) {
                    position = Vector2(
                        randomValue(0, screenWidth).toFloat(),
                        randomValue(0, screenHeight).toFloat()
                    )
                }
                if (frameCount > 150) {
                    // make itself disappear
                    state.dispose()
                }
            }

            onDraw {
                if (position != null) drawCircle(position, radius.toFloat(), color)
            }
        }
    }
}