package me.raylib.sample.audio

import io.github.andannn.raylib.foundation.Colors.LIGHTGRAY
import io.github.andannn.raylib.foundation.KeyboardKey
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.update
import io.github.andannn.raylib.runtime.remember
import io.github.andannn.raylib.foundation.sound

fun ComponentRegistry.soundLoading() = component("sound loading") {
    val fxWav = remember {
        sound("resources/sound.wav")
    }
    val fxOgg = remember {
        sound("resources/target.ogg")
    }

    update {
        if (KeyboardKey.KEY_SPACE.isPressed()) {
            playSound(fxWav)
        }

        if (KeyboardKey.KEY_ENTER.isPressed()) {
            playSound(fxOgg)
        }
    }

    draw {
        drawText("Press SPACE to PLAY the WAV sound!", 200, 180, 20, LIGHTGRAY)
        drawText("Press ENTER to PLAY the OGG sound!", 200, 220, 20, LIGHTGRAY)
    }
}