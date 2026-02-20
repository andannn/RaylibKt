package raylib.core

inline fun mainGameLoop(block: () -> Unit) {
    while (!RlWindow.shouldClose()) {
        block()
    }
    RlWindow.close()
}

inline fun drawScope(block: DrawScope.() -> Unit) {
    DrawScope.begin()
    block(DrawScope)
    DrawScope.end()
}

inline fun DrawScope.textDrawScope(block: TextDrawScope.() -> Unit) {
    block(TextDrawScope)
}