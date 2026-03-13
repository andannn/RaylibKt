/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.component
import io.github.andannn.raylib.core.find
import io.github.andannn.raylib.core.internal.buildComponents
import io.github.andannn.raylib.core.remember
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WorldGrid2DComponentTest {
    @Test
    fun worldGrid2DComponent_retained() {
        class FakeEntity(val state: Spatial2D) : Entity

        var state: Any? = null
        var context: WorldGrid2DContext? = null
        var isDisposed = false
        val control = buildComponents(
            block = {
                world2DGridComponent("", 30) {
                    context = find<WorldGrid2DContext>()
                    if (!isDisposed) {
                        component("child") {
                            state = remember {
                                FakeEntity(
                                    Spatial2DAlloc(
                                        size = Vector2(20f, 20f),
                                        position = Vector2(40f, 40f)
                                    )
                                )
                            }
                            registerEntityToWorldGrid2D(state, state.state)
                        }
                    }
                }
            }
        )

        control.rebuild()
        assertTrue(context!!.queryAll().any { it.entity == state })

        isDisposed = true
        control.rebuild()
        assertFalse(context.queryAll().any { it.entity == state })
    }
}