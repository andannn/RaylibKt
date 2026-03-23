/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.RememberScope
import kotlinx.cinterop.CValue
import raylib.interop.LoadMusicStream
import raylib.interop.LoadSound
import raylib.interop.LoadSoundFromWave
import raylib.interop.UnloadMusicStream
import raylib.interop.UnloadSound

fun RememberScope.musicStream(fileName: String): CValue<Music> {
    val stream = LoadMusicStream(fileName)

    disposeOnClose {
        UnloadMusicStream(stream)
    }

    return stream
}

fun RememberScope.sound(fileName: String): CValue<Sound> {
    val sound = LoadSound(fileName)

    disposeOnClose {
        UnloadSound(sound)
    }

    return sound
}

fun RememberScope.soundFromWave(wave: CValue<Wave>): CValue<Sound> {
    val sound = LoadSoundFromWave(wave)

    disposeOnClose {
        UnloadSound(sound)
    }
    return sound
}