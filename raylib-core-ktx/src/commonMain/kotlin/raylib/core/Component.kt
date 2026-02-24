package raylib.core

import kotlinx.cinterop.Arena
import kotlinx.cinterop.NativePlacement
import raylib.core.internal.DiffCallback
import raylib.core.internal.executeDiff


abstract class Component(val componentId: Any) : LoopHandler, Disposable {

}

internal fun Component(
    componentId: Any,
    handler: LoopHandler,
    scope: Disposable
): Component = object : Component(componentId), LoopHandler by handler, Disposable by scope {}

interface ComponentFactory {
    fun component(componentId: Any, block: ComponentScope.() -> LoopHandler)
}

interface ComponentManager : Disposable {
    fun buildComponentsIfNeeded()
    fun performUpdate(deltaTime: Float, scope: GameScope)
    fun performDraw(scope: DrawScope)
}

internal class ComponentManagerImpl(
    private val isDirty: () -> Boolean,
    private val onRebuildFinished: () -> Unit,
    private val block: ComponentFactory.() -> Unit
) : ComponentManager {
    internal val components = mutableListOf<Component>()

    override fun buildComponentsIfNeeded() {
        if (components.isEmpty() || isDirty()) {
            buildComponents()
            onRebuildFinished()
        }
    }

    private class KeyWithBuilder(
        val componentId: Any,
        val block: () -> Component
    )

    private inner class Differ(
        val before: List<Component>
    ) : ComponentFactory, DiffCallback {
        val after = mutableListOf<KeyWithBuilder>()
        private val componentKeys = mutableSetOf<Any>()
        override fun component(componentId: Any, block: ComponentScope.() -> LoopHandler) {
            require(componentKeys.add(componentId)) {
                "Error: Duplicate component key detected -> '$componentId'. " +
                        "Each component in the same scope must have a unique ID."
            }
            after.add(
                KeyWithBuilder(
                    componentId = componentId,
                    block = {
                        val scope = ComponentScope()
                        val handler = block(scope)
                        Component(componentId, handler, scope)
                    }
                )
            )
        }

        override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
            return before[oldIndex].componentId == after[newIndex].componentId
        }

        override fun insert(newIndex: Int) {
            println("insert ${after[newIndex].componentId} $newIndex")
            components.add(newIndex, after[newIndex].block())
        }

        override fun remove(atIndex: Int, oldIndex: Int) {
            println("remove ${before[oldIndex].componentId} $atIndex")
            components.removeAt(atIndex).dispose()
        }

        override fun same(oldIndex: Int, newIndex: Int) {
            println("same $oldIndex $newIndex")
        }
    }

    private fun buildComponents() {
        Differ(components.toList()).apply(block)
            .also { newComponentsBuilder ->
                executeDiff(
                    oldSize = components.size,
                    newSize = newComponentsBuilder.after.size,
                    callback = newComponentsBuilder
                )
            }
    }

    override fun performDraw(scope: DrawScope) {
        components.forEach { handler ->
            with(handler) {
                scope.draw()
            }
        }
    }

    override fun performUpdate(deltaTime: Float, scope: GameScope) {
        components.forEach { handler ->
            with(handler) { scope.update(deltaTime) }
        }
    }

    override fun dispose() {
        components.forEach { it.dispose() }
        components.clear()
    }
}

@MustUseReturnValues
class ComponentScope(
    private val arena: Arena = Arena()
) : NativePlacement by arena, Disposable {

    inline fun provideHandlers(
        crossinline block: LoopHandlerBuilder.() -> Unit,
    ): LoopHandler {
        return LoopHandlerBuilder().apply(block).build()
    }

    override fun dispose(): Unit = arena.clear()
}
