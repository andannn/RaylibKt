package raylib.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import raylib.core.internal.DiffCallback
import raylib.core.internal.executeDiff
import kotlin.coroutines.CoroutineContext
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

internal abstract class Component(val componentId: Any) : LoopHandler, Disposable {
    @OptIn(ExperimentalNativeApi::class)
    val cleaner = createCleaner(componentId) {
        println("Runtime Monitor: Component [$it] has been Garbage Collected.")
    }
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
    fun performUpdate(deltaTime: Float, scope: GameContext)
    fun performDraw(scope: DrawContext)
}

internal class ComponentManagerImpl(
    private val windowFunction: WindowFunction,
    private val isDirty: () -> Boolean,
    private val onRebuildFinished: () -> Unit,
    private val block: ComponentFactory.() -> Unit
) : ComponentManager {
    internal val components = mutableListOf<Component>()

    private val coroutineContext = CoroutineScope(Dispatchers.Default)
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
                        val scope = ComponentScope(windowFunction, coroutineContext.coroutineContext)
                        val handler = block(scope)
                        Component(componentId, handler) {
                            scope.dispose()
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

    override fun performDraw(scope: DrawContext) {
        components.forEach { handler ->
            with(handler) { draw(scope) }
        }
    }

    override fun performUpdate(deltaTime: Float, scope: GameContext) {
        components.forEach { handler ->
            with(handler) { update(scope, deltaTime) }
        }
    }

    override fun dispose() {
        coroutineContext.cancel()
        components.forEach {
            it.dispose()
        }
        components.clear()
    }
}

@MustUseReturnValues
class ComponentScope(
    private val windowFunction: WindowFunction,
    parentContext: CoroutineContext,
) : DisposableRegistry, WindowFunction by windowFunction {
    private val disposableRegistry = DisposableRegistryImpl()

    val scope = CoroutineScope(parentContext + Job())

    inline fun provideHandlers(
        crossinline block: LoopHandlerBuilder.() -> Unit,
    ): LoopHandler {
        return LoopHandlerBuilder(scope).apply(block).build()
    }

    override fun disposeOnClose(disposable: Disposable) = disposableRegistry.disposeOnClose(disposable)

    internal fun dispose() {
        scope.cancel()
        disposableRegistry.dispose()
    }
}
