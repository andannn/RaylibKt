package raylib.core

import kotlinx.cinterop.CValue

fun randomValue(min: Int, max: Int) = raylib.interop.GetRandomValue(min, max)

fun randomColor(): CValue<Color> = Color(
    randomValue(0, 255),
    randomValue(0, 255),
    randomValue(0, 255),
    255
)