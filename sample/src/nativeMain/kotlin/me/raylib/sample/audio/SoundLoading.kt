package me.raylib.sample.audio

import io.github.andannn.raylib.base.Colors.LIGHTGRAY
import io.github.andannn.raylib.base.KeyboardKey
import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.update
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.core.sound

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