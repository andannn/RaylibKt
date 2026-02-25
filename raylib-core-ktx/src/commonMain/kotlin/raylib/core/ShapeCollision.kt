package raylib.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.readValue

fun CValue<Vector2>.isCollisionWith(rec: CValue<Rectangle>): Boolean =
    raylib.interop.CheckCollisionPointRec(this, rec)

fun Vector2.isCollisionWith(rec: CValue<Rectangle>): Boolean =
    this.readValue().isCollisionWith(rec)

fun CValue<Vector2>.isCollisionWith(
    center: CValue<Vector2>,
    radius: Float
): Boolean =
    raylib.interop.CheckCollisionPointCircle(this, center, radius)

fun Vector2.isCollisionWith(
    center: CValue<Vector2>,
    radius: Float
): Boolean =
    readValue().isCollisionWith(center, radius)

fun CValue<Rectangle>.isCollisionWith(
    rect: CValue<Rectangle>
): Boolean = raylib.interop.CheckCollisionRecs(this, rect)

fun Rectangle.isCollisionWith(
    rect: CValue<Rectangle>
): Boolean = readValue().isCollisionWith(rect)


fun CValue<Rectangle>.getCollisionRec(rec2: CValue<Rectangle>): CValue<Rectangle> =
    raylib.interop.GetCollisionRec(this, rec2)

fun Rectangle.getCollisionRec(rec2: CValue<Rectangle>): CValue<Rectangle> =
    this.readValue().getCollisionRec(rec2)
