/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.easings

import io.github.andannn.raylib.runtime.SuspendingUpdateEventScope
import io.github.andannn.raylib.runtime.awaitDuration
import kotlin.time.Duration

context(scope: SuspendingUpdateEventScope)
suspend fun awaitEasingAnimation(
    start: Float,
    target: Float,
    duration: Duration,
    easing: Easing = Ease.LinearNone,
    onUpdate: (Float) -> Unit
) = awaitDuration(duration) { fraction ->
    onUpdate(start.animateTo(target, fraction, easing))
}

