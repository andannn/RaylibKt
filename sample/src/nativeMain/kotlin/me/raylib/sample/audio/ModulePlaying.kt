package me.raylib.sample.audio

import io.github.andannn.raylib.foundation.Colors.GRAY
import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.getValue
import io.github.andannn.raylib.foundation.musicStream
import io.github.andannn.raylib.runtime.mutableStateOf
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.runtime.setValue
import io.github.andannn.raylib.foundation.windowContext
import kotlinx.cinterop.copy

fun ComponentRegistry.modulePlaying() = component("module playing") {
    val music = remember {
        musicStream("resources/mini1111.xm").also {
            windowContext.playMusicStream(it.copy { looping = false })
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

    update {
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

    draw {
        drawRectangle(20, screenHeight - 20 - 12, screenWidth - 40, 12, LIGHTGRAY)
        drawRectangle(20, screenHeight - 20 - 12, (timePlayedFactor * (screenWidth - 40)).toInt(), 12, MAROON)
        drawRectangleLines(20, screenHeight - 20 - 12, screenWidth - 40, 12, GRAY)
    }
}