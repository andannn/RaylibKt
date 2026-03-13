package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.Music
import io.github.andannn.raylib.base.Sound
import kotlinx.cinterop.CValue

fun RememberScope.musicStream(fileName: String): CValue<Music> {
    val stream = raylib.interop.LoadMusicStream(fileName)

    disposeOnClose {
        raylib.interop.UnloadMusicStream(stream)
    }

    return stream
}

fun RememberScope.sound(fileName: String): CValue<Sound> {
    val sound = raylib.interop.LoadSound(fileName)

    disposeOnClose {
        raylib.interop.UnloadSound(sound)
    }

    return sound
}