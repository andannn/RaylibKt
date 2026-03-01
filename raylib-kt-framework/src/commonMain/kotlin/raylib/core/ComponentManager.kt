package raylib.core

import raylib.core.internal.DiffCallback
import raylib.core.internal.executeDiff


interface ComponentManager : Disposable {
    fun buildComponentsIfNeeded()
    fun performUpdate(deltaTime: Float)
    fun performDraw()
}

interface ComponentFactory {
    fun component(componentId: Any, block: ComponentScope.() -> LoopHandler)
}

internal class ComponentManagerImpl(
    private val contextRegistry: ContextRegistry,
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
        val builder: () -> Component
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
                    builder = {
                        Component(componentId, contextRegistry).apply {
                            loopHandler = block()
                        }
                    }
                )
            )
        }

        override fun areItemsTheSame(oldIndex: Int, newIndex: Int): Boolean {
            return before[oldIndex].componentId == after[newIndex].componentId
        }

        override fun insert(newIndex: Int) {
            println("insert ${after[newIndex].componentId} $newIndex")
            components.add(newIndex, after[newIndex].builder())
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

    override fun performDraw() {
        components.forEach { component ->
            with(component.requireLoopHandler()) { draw() }
        }
    }

    override fun performUpdate(deltaTime: Float) {
        components.forEach { component ->
            with(component.requireLoopHandler()) { update(deltaTime) }
        }
    }

    override fun dispose() {
        components.forEach {
            it.dispose()
        }
        components.clear()
    }
}
