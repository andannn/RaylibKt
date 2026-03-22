/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import io.github.andannn.raylib.runtime.Context


interface DrawContext : Context, WindowContext, BasicShapeDrawFunction, TextDrawFunction, TextureDrawFunction

internal fun DrawContext(
    windowContext: WindowContext,
    basicShapeDrawFunction: BasicShapeDrawFunction = BasicShapeDrawFunction(),
    textDrawFunction: TextDrawFunction = TextDrawFunction(),
    textureDrawFunction: TextureDrawFunction = TextureDrawFunction(),
): DrawContext = object : DrawContext,
    WindowContext by windowContext,
    BasicShapeDrawFunction by basicShapeDrawFunction,
    TextDrawFunction by textDrawFunction,
    TextureDrawFunction by textureDrawFunction{}

