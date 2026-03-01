package raylib.core

import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.CValue
import kotlinx.cinterop.CVariable
import kotlinx.cinterop.useContents

fun randomValue(min: Int, max: Int) = raylib.interop.GetRandomValue(min, max)

fun randomColor(): CValue<Color> = Color(
    randomValue(0, 255),
    randomValue(0, 255),
    randomValue(0, 255),
    255
)

inline fun <reified T : CStructVar> Iterable<CValue<T>>.forEachContents(
    action: (T) -> Unit
) {
    forEach {
        it.useContents<T, Unit> {
            action(this)
        }
    }
}

inline fun <reified T : CStructVar> Iterable<CValue<T>>.forEachContentsIndexed(
    action: (index: Int, T) -> Unit
) {
    forEachIndexed { index, value ->
        value.useContents<T, Unit> {
            action(index, this)
        }
    }
}