/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.base

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue
import raylib.interop.CheckCollisionPointCircle
import raylib.interop.CheckCollisionPointRec
import raylib.interop.CheckCollisionRecs
import raylib.interop.GetCollisionRec

fun CValue<Vector2>.isCollisionWith(rec: CValue<Rectangle>): Boolean =
    CheckCollisionPointRec(this, rec)

fun Vector2.isCollisionWith(rec: CValue<Rectangle>): Boolean =
    this.readValue().isCollisionWith(rec)

fun CValue<Vector2>.isCollisionWith(
    center: CValue<Vector2>,
    radius: Float
): Boolean =
    CheckCollisionPointCircle(this, center, radius)

fun Vector2.isCollisionWith(
    center: CValue<Vector2>,
    radius: Float
): Boolean =
    readValue().isCollisionWith(center, radius)

fun CValue<Rectangle>.isCollisionWith(
    rect: CValue<Rectangle>
): Boolean = CheckCollisionRecs(this, rect)

fun Rectangle.isCollisionWith(
    rect: CValue<Rectangle>
): Boolean = readValue().isCollisionWith(rect)


fun CValue<Rectangle>.getCollisionRec(rec2: CValue<Rectangle>): CValue<Rectangle> =
    GetCollisionRec(this, rec2)

fun Rectangle.getCollisionRec(rec2: CValue<Rectangle>): CValue<Rectangle> =
    this.readValue().getCollisionRec(rec2)
