package raylib.core

inline fun rlMatrix(block : () -> Unit) {
    raylib.interop.rlPushMatrix()
    block()
    raylib.interop.rlPopMatrix()
}