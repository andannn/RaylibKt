/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.gui

import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.Context
import io.github.andannn.raylib.core.ContextRegistry
import io.github.andannn.raylib.core.DrawContext
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.draw

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
