/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core.internal

import io.github.andannn.raylib.core.ComponentRegistry
import io.github.andannn.raylib.core.ContextRegistryInternal
import io.github.andannn.raylib.core.DrawContext
import io.github.andannn.raylib.core.GameContext
import io.github.andannn.raylib.core.RootComponent
import io.github.andannn.raylib.core.WindowContext
import io.github.andannn.raylib.core.provide

interface RebuildControl {
    fun rebuild()
}

fun buildComponents(
    block: ComponentRegistry.() -> Unit
): RebuildControl {
    val root = rootComponent(block)
    return object : RebuildControl {
        override fun rebuild() {
            root.buildComponents()
        }
    }
}

internal fun rootComponent(block: ComponentRegistry.() -> Unit): RootComponent {
    val windowContext = DummyWindowContextImpl()
    val gameContext = GameContext()
    val drawContext = DrawContext()
    return RootComponent(
        contextRegistry = ContextRegistryInternal(),
        windowContext,
        block = {
            provide<WindowContext>(windowContext) {
                provide(gameContext) {
                    provide(drawContext) {
                        block()
                    }
                }
            }
        }
    )
}