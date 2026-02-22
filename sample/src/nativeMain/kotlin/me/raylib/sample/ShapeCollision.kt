package me.raylib.sample

import kotlinx.cinterop.CValue
import raylib.core.Rectangle
import raylib.core.Vector2

fun CValue<Vector2>.isCollisionWith(rec: CValue<Rectangle>): Boolean =
    raylib.interop.CheckCollisionPointRec(this, rec)
