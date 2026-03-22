/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.gui

import io.github.andannn.raylib.foundation.DrawContext
import io.github.andannn.raylib.foundation.draw
import io.github.andannn.raylib.runtime.ComponentScope
import io.github.andannn.raylib.runtime.Context
import io.github.andannn.raylib.runtime.ContextRegistry
import io.github.andannn.raylib.runtime.find

fun ComponentScope.drawGui(block: GuiContext.() -> Unit) {
    draw {
        with(find<GuiContext>()) {
            block()
        }
    }
}

interface GuiContext : Context, GuiFunction, DrawContext

fun ContextRegistry.GuiContext(): GuiContext =
    object : GuiContext, GuiFunction by GuiFunction(), DrawContext by find<DrawContext>() {}
