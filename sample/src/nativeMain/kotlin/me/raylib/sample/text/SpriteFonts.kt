package me.raylib.sample.text

import io.github.andannn.raylib.foundation.Colors.DARKBLUE
import io.github.andannn.raylib.foundation.Colors.DARKGRAY
import io.github.andannn.raylib.foundation.Colors.DARKGREEN
import io.github.andannn.raylib.foundation.Colors.DARKPURPLE
import io.github.andannn.raylib.foundation.Colors.GOLD
import io.github.andannn.raylib.foundation.Colors.LIME
import io.github.andannn.raylib.foundation.Colors.MAROON
import io.github.andannn.raylib.foundation.Colors.ORANGE
import io.github.andannn.raylib.foundation.Colors.RED
import io.github.andannn.raylib.foundation.Font
import io.github.andannn.raylib.foundation.Vector2Alloc
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.foundation.forEachContentsIndexed
import io.github.andannn.raylib.foundation.loadFont
import io.github.andannn.raylib.foundation.screenWidth
import io.github.andannn.raylib.runtime.ComponentRegistry
import io.github.andannn.raylib.runtime.component
import io.github.andannn.raylib.runtime.doOnce
import io.github.andannn.raylib.runtime.remember
import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import raylib.interop.MeasureTextEx

fun ComponentRegistry.spriteFonts() = component("font") {
    val fonts = remember {
        mutableListOf<CValue<Font>>().apply {
            add(loadFont("resources/sprite_fonts/alagard.png"))
            add(loadFont("resources/sprite_fonts/pixelplay.png"))
            add(loadFont("resources/sprite_fonts/mecha.png"))
            add(loadFont("resources/sprite_fonts/setback.png"))
            add(loadFont("resources/sprite_fonts/romulus.png"))
            add(loadFont("resources/sprite_fonts/pixantiqua.png"))
            add(loadFont("resources/sprite_fonts/alpha_beta.png"))
            add(loadFont("resources/sprite_fonts/jupiter_crash.png"))
        }
    }

    val messages = remember {
        listOf(
            "LAGARD FONT designed by Hewett Tsoi",
            "PIXELPLAY FONT designed by Aleksander Shevchuk",
            "MECHA FONT designed by Captain Falcon",
            "SETBACK FONT designed by Brian Kent (AEnigma)",
            "ROMULUS FONT designed by Hewett Tsoi",
            "PIXANTIQUA FONT designed by Gerhard Grossmann",
            "ALPHA_BETA FONT designed by Brian Kent (AEnigma)",
            "JUPITER_CRASH FONT designed by Brian Kent (AEnigma)",
        )
    }
    val spacings = remember { listOf(2f, 4f, 8f, 4f, 3f, 4f, 4f, 1f) }

    val positions = remember {
        List(8) { i ->
            fonts[i].useContents {
                val measured = MeasureTextEx(fonts[i], messages[i], baseSize * 2.0f, spacings[i])
                Vector2Alloc(
                    x = screenWidth / 2.0f - measured.useContents { x } / 2.0f,
                    y = 60.0f + baseSize + 45.0f * i
                ).value
            }
        }
    }
    doOnce {
        positions[3].y += 8
        positions[4].y += 2
        positions[7].y -= 8
    }

    val colors = remember {
        listOf(MAROON, ORANGE, DARKGREEN, DARKBLUE, DARKPURPLE, LIME, GOLD, RED)
    }

    draw {
        drawText("free sprite fonts included with raylib", 220, 20, 20f, DARKGRAY)
        drawLine(220, 50, 600, 50, DARKGRAY);

        fonts.forEachContentsIndexed { i, font ->
            drawText(
                messages[i],
                positions[i].readValue(),
                font.baseSize * 2.0f,
                colors[i],
                spacings[i],
                font.readValue(),
            )
        }
    }
}