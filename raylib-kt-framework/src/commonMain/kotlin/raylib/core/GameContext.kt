/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core

interface GameContext :Context, KeyboardFunction, MouseFunction, GestureFunction, GamepadFunction

internal fun GameContext(
    keyboardFunction: KeyboardFunction = KeyboardFunction(),
    mouseFunction: MouseFunction = MouseFunction(),
    gestureFunction: GestureFunction = GestureFunction(),
    gamepadFunction: GamepadFunction = GamepadFunction()
): GameContext = object :
    GameContext,
    KeyboardFunction by keyboardFunction,
    MouseFunction by mouseFunction,
    GestureFunction by gestureFunction,
    GamepadFunction by gamepadFunction {}
