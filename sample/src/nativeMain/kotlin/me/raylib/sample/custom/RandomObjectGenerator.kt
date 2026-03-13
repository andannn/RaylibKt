package me.raylib.sample.custom

import kotlinx.cinterop.readValue
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.NativeState
import io.github.andannn.raylib.base.Vector2Alloc
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.base.randomColor
import io.github.andannn.raylib.base.randomValue
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.components
import io.github.andannn.raylib.core.mutableStateListOf
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.nativeStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue

fun ComponentRegistry.randomObjectGenerator() {
    val stateList = remember {
        mutableStateListOf<Int>()
    }

    component("random object generator") {
        var frameCount by remember {
            mutableStateOf(0)
        }
        var newId by remember {
            mutableStateOf(0)
        }
        onUpdate {
            frameCount++
            if (frameCount % 60 == 0) {
                if (stateList.size <= 10000) {
                    stateList.addState { newId++ }
                }
            }
        }
    }

    components(stateList, { it }) { state ->
        generatedObject(state)
    }
}

private fun ComponentScope.generatedObject(state: NativeState<Int>) {
    var frameCount by remember {
        mutableStateOf(0f)
    }
    val color = remember { randomColor() }
    val radius = remember { randomValue(1, 10) }
    val position by remember {
        nativeStateOf {
            Vector2Alloc(
                randomValue(0, screenWidth).toFloat(),
                randomValue(0, screenHeight).toFloat()
            )
        }
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