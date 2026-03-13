package me.raylib.sample.audio

import io.github.andannn.raylib.base.Colors.GRAY
import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.Colors.MAROON
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.getValue
import io.github.andannn.raylib.core.musicStream
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.onDraw
import io.github.andannn.raylib.core.onUpdate
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.setValue
import kotlinx.cinterop.copy

fun ComponentRegistry.modulePlaying() = component("module playing") {
    val music = remember {
        musicStream("resources/mini1111.xm").also {
            playMusicStream(it.copy { looping = false })
        }
    }
    var pause by remember {
        mutableStateOf(false)
    }
    var pitch by remember {
        mutableStateOf(1f)
    }
    var timePlayedFactor by remember {
        mutableStateOf(0f)
    }

    onUpdate {
        updateMusicStream(music)

        if (KeyboardKey.KEY_SPACE.isPressed()) {
            stopMusicStream(music)
            playMusicStream(music)
            pause = false
        }

        if (KeyboardKey.KEY_P.isPressed()) {
            pause = !pause
            if (pause) pauseMusicStream(music)
            else resumeMusicStream(music)
        }

        if (KeyboardKey.KEY_UP.isPressed()) {
            pitch += 0.01f
        }
        if (KeyboardKey.KEY_DOWN.isPressed()) {
            pitch -= 0.01f
        }
        setMusicPitch(music, pitch)

        timePlayedFactor = getMusicTimePlayed(music)/getMusicTimeLength(music)
    }

    onDraw {
        drawRectangle(20, screenHeight - 20 - 12, screenWidth - 40, 12, LIGHTGRAY)
        drawRectangle(20, screenHeight - 20 - 12, (timePlayedFactor * (screenWidth - 40)).toInt(), 12, MAROON)
        drawRectangleLines(20, screenHeight - 20 - 12, screenWidth - 40, 12, GRAY)
    }
}