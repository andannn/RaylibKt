package raylib.core

fun drawScope(block: DrawScope.() -> Unit) {
    DrawScope.begin()
    block(DrawScope)
    DrawScope.end()
}

fun DrawScope.text(block: TextDrawScope.() -> Unit) {
    block(TextDrawScope)
}