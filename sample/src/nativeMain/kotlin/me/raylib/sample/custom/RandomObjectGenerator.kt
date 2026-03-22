package me.raylib.sample.custom

import kotlinx.cinterop.readValue
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.NativeState
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.randomColor
import io.github.andannn.raylib.foundation.randomValue
import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.foundation.Vector2Alloc
import io.github.andannn.raylib.runtime.components
import io.github.andannn.raylib.runtime.mutableStateListOf
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.screenHeight
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.setValue

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
        update {
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
        Vector2Alloc(
            randomValue(0, screenWidth).toFloat(),
            randomValue(0, screenHeight).toFloat()
        )
    }

    update {
        frameCount++

        if (frameCount > 60) {
            // make itself disappear
            state.dispose()
        }
    }

    draw {
        drawCircle(position.readValue(), radius.toFloat(), color)
    }
}