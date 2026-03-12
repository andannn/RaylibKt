/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.base.matrixIdentity
import io.github.andannn.raylib.base.matrixRotateZ
import io.github.andannn.raylib.base.matrixScale
import io.github.andannn.raylib.base.matrixTranslate
import io.github.andannn.raylib.base.multiply
import io.github.andannn.raylib.core.internal.buildComponents
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.remember
import reasings.interop.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class Transform2DComponentTest {
    @Test
    fun transform2D_get_worldMatrix_Test() {
        val control = buildComponents {
            val transform = remember {
                Transform2DAlloc(
                    position = Vector2(100f, 100f), scale = Vector2(2f, 3f), offset = Vector2(120f, 240f),
                    angle = mutableStateOf(120f)
                )
            }
            transform2DComponent(
                "",
                transform
            ) {
                val expect = matrixIdentity()
                    .multiply(matrixTranslate(100f, 100f))
                    .multiply(matrixRotateZ(120f / 180f * PI))
                    .multiply(matrixScale(2f, 3f))
                    .multiply(matrixTranslate(120f, 240f))
                assertEquals(
                    expect,
                    worldMatrix()
                )
            }
        }

        control.rebuild()
    }

    @Test
    fun transform2D_get_worldMatrix_in_grand_child_Test() {
        val control = buildComponents {
            val transform = remember {
                Transform2DAlloc(
                    position = Vector2(100f, 100f), scale = Vector2(2f, 3f), offset = Vector2(120f, 240f),
                    angle = mutableStateOf(120f)
                )
            }
            transform2DComponent(
                "",
                transform
            ) {
                val transform2 = remember {
                    Transform2DAlloc(position = Vector2(100f, 100f), angle = mutableStateOf(90f))
                }
                val expect = matrixIdentity()
                    .multiply(matrixTranslate(100f, 100f))
                    .multiply(matrixRotateZ(120f / 180f * PI))
                    .multiply(matrixScale(2f, 3f))
                    .multiply(matrixTranslate(120f, 240f))
                // Transform in child
                    .multiply(matrixTranslate(100f, 100f))
                    .multiply(matrixRotateZ(90f / 180f * PI))

                transform2DComponent(
                    "",
                    transform2
                ) {
                    assertEquals(
                        expect,
                        worldMatrix()
                    )
                }
            }
        }

        control.rebuild()
    }
}
