package raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement
import raylib.core.internal.DiffCallback
import raylib.core.internal.executeDiff


abstract class GameComponent(val componentId: Any) : LoopHandler, Disposable {

}

internal fun GameComponent(
    componentId: Any,
    handler: LoopHandler,
    scope: Disposable
): GameComponent = object : GameComponent(componentId), LoopHandler by handler, Disposable by scope {}

interface GameComponentsRegisterScope {
    fun component(componentId: Any, block: GameComponentScope.() -> LoopHandler)
}

interface GameComponentManager : Disposable {
    fun buildComponentsIfNeeded()
    fun performUpdate(scope: GameScope)
    fun performDraw(scope: DrawScope)
}

internal class GameComponentManagerImpl(
    private val isDirty: () -> Boolean,
    private val onRebuildFinished: () -> Unit,
    private val block: GameComponentsRegisterScope.() -> Unit
) : GameComponentManager {
    internal val gameComponents = mutableListOf<GameComponent>()

    override fun buildComponentsIfNeeded() {
        if (gameComponents.isEmpty() || isDirty()) {
            buildComponents()
            onRebuildFinished()
        }
    }

    private class KeyWithBuilder(
        val componentId: Any,
        val block: () -> GameComponent
    )

    private inner class Differ(
        val before: List<GameComponent>
    ) : GameComponentsRegisterScope, DiffCallback {
        val after = mutableListOf<KeyWithBuilder>()
        private val componentKeys = mutableSetOf<Any>()
        override fun component(componentId: Any, block: GameComponentScope.() -> LoopHandler) {
            require(componentKeys.add(componentId)) {
                "Error: Duplicate component key detected -> '$componentId'. " +
                        "Each component in the same scope must have a unique ID."
            }
            after.add(
                KeyWithBuilder(
                    componentId = componentId,
                    block = {
                        val scope = GameComponentScope()
                        val handler = block(scope)
                        GameComponent(componentId, handler, scope)
                    }
                )
            )
        }

        override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
            return before[oldIndex].componentId == after[newIndex].componentId
        }

        override fun insert(newIndex: Int) {
            println("insert ${after[newIndex].componentId} $newIndex")
            gameComponents.add(newIndex, after[newIndex].block())
        }

        override fun remove(atIndex: Int, oldIndex: Int) {
            println("remove ${before[oldIndex].componentId} $atIndex")
            gameComponents.removeAt(atIndex).dispose()
        }

        override fun same(oldIndex: Int, newIndex: Int) {
            println("same $oldIndex $newIndex")
        }
    }

    private fun buildComponents() {
        Differ(gameComponents.toList()).apply(block)
            .also { newComponentsBuilder ->
                executeDiff(
                    oldSize = gameComponents.size,
                    newSize = newComponentsBuilder.after.size,
                    callback = newComponentsBuilder
                )
            }
    }

    override fun performDraw(scope: DrawScope) {
        gameComponents.forEach { handler ->
            with(handler) {
                scope.draw()
            }
        }
    }

    override fun performUpdate(scope: GameScope) {
        gameComponents.forEach { handler ->
            with(handler) { scope.update() }
        }
    }

    override fun dispose() {
        gameComponents.forEach { it.dispose() }
        gameComponents.clear()
    }
}

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
