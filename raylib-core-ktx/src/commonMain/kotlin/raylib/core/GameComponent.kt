package raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement


abstract class GameComponent : LoopHandler, Disposable {

}

internal fun GameComponent(
    handler: LoopHandler,
    scope: Disposable
): GameComponent = object : GameComponent(), LoopHandler by handler, Disposable by scope {}

class GameComponentScope(
    private val arena: Arena = Arena()
) : NativePlacement by arena, Disposable {

    inline fun provideHandlers(
        crossinline block: LoopHandlerBuilder.() -> Unit,
    ): LoopHandler {
        return LoopHandlerBuilder().apply(block).build()
    }

    override fun dispose(): Unit = arena.clear()
}
