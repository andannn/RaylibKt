package raylib.core

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CValue
import kotlinx.cinterop.ptr
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents

inline fun <reified R> RememberScope.useImage(
    fileName: String,
    crossinline convert: (CPointer<Image>) -> Unit = {},
    crossinline block: (CValue<Image>) -> R
): R {
    val image = loadImage(fileName)
    val convertedImage = image.useContents {
        val temp = this
        convert(temp.ptr)
        temp.readValue()
    }
    return try {
        block(convertedImage)
    } finally {
        unLoadImage(convertedImage)
    }
}