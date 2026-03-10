/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.core

import io.github.andannn.raylib.base.WindowFunction
import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

interface ComponentRegistry: ContextRegistry

inline fun ComponentRegistry.component(id: Any, crossinline block: ComponentScope.() -> Unit) {
    (this as ComponentStore)

    val component = getCachedOrCreateComponent(id)

    // apply block to build children components.
    component.block()
    component.onEndBuildComponents()

    finishComponent(id, component)
}

inline fun ComponentRegistry.doOnce(crossinline block: RememberScope.() -> Unit) {
    remember {
        block()
    }
}

inline fun <reified R> ComponentRegistry.remember(block: RememberScope.() -> R): R {
    (this as ComponentStore)

    return rememberedValue().let {
        if (it == null) {
            val value = block(rememberScope)
            updateRememberedValue(value)
            value
        } else it
    } as R
}

interface ComponentScope : WindowFunction, DisposableRegistry, ComponentRegistry, ContextProvider

inline fun ComponentScope.onUpdate(crossinline block: GameContext.(Float) -> Unit) {
    if (find<WindowContext>().renderPhase == RenderPhase.UPDATE) {
        block(find<GameContext>(), find<WindowContext>().frameTimeSeconds)
    }
}

inline fun ComponentScope.onDraw(crossinline block: DrawContext.() -> Unit) {
    if (find<WindowContext>().renderPhase == RenderPhase.DRAW) {
        block(find<DrawContext>())
    }
}

interface RememberScope : DisposableRegistry, WindowContext

internal fun RememberScope(
    disposableRegistry: DisposableRegistry,
    contextRegistry: ContextRegistry,
    windowContext: WindowContext = contextRegistry.find<WindowContext>(),
): RememberScope = object : RememberScope, DisposableRegistry by disposableRegistry,
    WindowContext by windowContext {}

internal fun interface UpdateHandler {
    fun performUpdate(deltaTime: Float)
}

internal class RootComponent(
    contextRegistry: ContextRegistryInternal,
    windowContext: WindowContext,
    private val block: ComponentRegistry.() -> Unit,
    ) : Component("root", contextRegistry, windowContext), Disposable {
    fun buildComponents() {
        buildComponents(block)
    }

    fun dumpDebugInfo() {
        println("------------------dump start------------------")
        println(debugInfo())
        println("------------------dump end--------------------")
    }
}

internal fun Component(
    id: Any,
    contextRegistry: ContextRegistryInternal,
): Component = object : Component(id, contextRegistry) {}

internal abstract class Component(
    val componentId: Any,
    private val contextRegistry: ContextRegistryInternal,
    private val windowContext: WindowContext = contextRegistry.find<WindowContext>(),
    private val disposableRegistry: DisposableRegistryImpl = DisposableRegistryImpl(),
    private val componentsBuilder: ComponentsBuilder = ComponentsBuilder(contextRegistry, disposableRegistry, windowContext)
) : ComponentScope,
    ComponentFactory by componentsBuilder,
    ComponentStore by componentsBuilder,
    ContextRegistryInternal by contextRegistry,
    DisposableRegistry by disposableRegistry,
    WindowFunction by windowContext,
    Disposable {
    internal val children: Iterable<Component>
        get() = componentsBuilder.activeStates.values

    @OptIn(ExperimentalNativeApi::class)
    private val cleaner = createCleaner(componentId) {
        println("Runtime Monitor: Component [$it] has been Garbage Collected.")
    }

    override fun dispose() {
        println("Component [${componentId}] has been disposed.")

        children.forEach {
            it.dispose()
        }
        disposableRegistry.dispose()
    }

    internal fun debugInfo(indent: Int = 0): String {
        val prefix = "  ".repeat(indent)
        return buildString {
            appendLine("${prefix}L component: $componentId")
            children.forEach {
                append(it.debugInfo(indent + 1))
            }
        }
    }

    override fun toString(): String {
        return "Component(componentId=$componentId)"
    }
}

@PublishedApi
internal interface ComponentFactory : ComponentRegistry {
    fun buildComponents(block: ComponentRegistry.() -> Unit)
}

@PublishedApi
internal interface ComponentStore {
    fun rememberedValue(): Any?
    fun updateRememberedValue(value: Any?)
    val rememberScope: RememberScope

    fun getCachedOrCreateComponent(id: Any): Component

    fun finishComponent(id: Any, component: Component)

    fun onEndBuildComponents()
}

internal class ComponentsBuilder(
    private val contextRegistry: ContextRegistryInternal,
    disposableRegistry: DisposableRegistry,
    windowContext: WindowContext = contextRegistry.find<WindowContext>(),
    override val rememberScope: RememberScope = RememberScope(disposableRegistry, contextRegistry, windowContext)
) : ComponentFactory, ComponentStore, ContextRegistryInternal by contextRegistry {
    var activeStates = HashMap<Any, Component>()
    private var pendingStates = HashMap<Any, Component>()
    private val componentKeys = mutableSetOf<Any>()

    private val rememberedStates = mutableListOf<Any?>()
    private var readIndex = 0

    override fun rememberedValue(): Any? {
        return if (readIndex < rememberedStates.size) {
            rememberedStates[readIndex++]
        } else {
            null
        }
    }

    override fun updateRememberedValue(value: Any?) {
        rememberedStates.add(value)
        readIndex++
    }

    override fun getCachedOrCreateComponent(id: Any): Component {
        checkUniqueKey(id)
        return activeStates.remove(id) ?: Component(id, contextRegistry)
    }

    override fun finishComponent(id: Any, component: Component) {
        pendingStates[id] = component
    }

    override fun buildComponents(block: ComponentRegistry.() -> Unit) {
        block.invoke(this)
        onEndBuildComponents()
    }

    override fun onEndBuildComponents() {
        componentKeys.clear()
        readIndex = 0

        if (activeStates.isNotEmpty()) {
            activeStates.values.forEach {
                it.dispose()
            }
            activeStates.clear()
        }

        val temp = activeStates
        activeStates = pendingStates
        pendingStates = temp
    }

    private fun checkUniqueKey(id: Any) {
        require(componentKeys.add(id)) {
            "Duplicate component key in same build scope: '$id' (${id::class})."
        }
    }
}
