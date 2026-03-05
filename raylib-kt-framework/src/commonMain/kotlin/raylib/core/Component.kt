package raylib.core

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

interface ComponentRegistry {
    fun <R> remember(block: RememberScope.() -> R): R
    fun component(id: Any, block: ComponentScope.() -> Unit)
}

interface ComponentScope : RememberScope, ComponentRegistry, ContextProvider {
    fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit)

    fun onDraw(block: DrawContext.() -> Unit)
}

interface RememberScope : DisposableRegistry, WindowFunction

internal fun RememberScope(
    disposableRegistry: DisposableRegistry,
    contextRegistry: ContextRegistry,
): RememberScope = object : RememberScope, DisposableRegistry by disposableRegistry,
    WindowFunction by contextRegistry.get<WindowContext>() {}

internal fun interface UpdateHandler {
    fun performUpdate(deltaTime: Float)
}

internal fun interface DrawHandler {
    fun performDraw()
}

internal interface LoopHandler : UpdateHandler, DrawHandler

internal class RootComponent(
    contextRegistry: ContextRegistry,
    private val block: ComponentRegistry.() -> Unit,
) : Component("root", contextRegistry), Disposable {
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
    contextRegistry: ContextRegistry,
): Component = object : Component(id, contextRegistry) {}

internal abstract class Component(
    val componentId: Any,
    private val contextRegistry: ContextRegistry,
    private val disposableRegistry: DisposableRegistryImpl = DisposableRegistryImpl(),
    private val componentsBuilder: ComponentsBuilder = ComponentsBuilder(contextRegistry, disposableRegistry)
) : ComponentScope,
    ComponentFactory by componentsBuilder,
    ContextRegistry by contextRegistry,
    DisposableRegistry by disposableRegistry,
    WindowFunction by contextRegistry.get<WindowContext>(),
    LoopHandler,
    Disposable {
    internal val children: Iterable<Component>
        get() = componentsBuilder.activeStates.values

    private var _loopHandler: LoopHandler? = null
    private val loopHandlerBuilder = LoopHandlerBuilder(contextRegistry)

    @OptIn(ExperimentalNativeApi::class)
    private val cleaner = createCleaner(componentId) {
        println("Runtime Monitor: Component [$it] has been Garbage Collected.")
    }

    override fun performUpdate(deltaTime: Float) {
        requireLoopHandler().performUpdate(deltaTime)

        children.forEach {
            it.performUpdate(deltaTime)
        }
    }

    override fun performDraw() {
        requireLoopHandler().performDraw()

        children.forEach {
            it.performDraw()
        }
    }

    override fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit) {
// TODO: rebuild _loopHandler
        if (_loopHandler != null) return
        loopHandlerBuilder.registerUpdateCallBack(block)
    }

    override fun onDraw(block: DrawContext.() -> Unit) {
// TODO: rebuild _loopHandler
        if (_loopHandler != null) return
        loopHandlerBuilder.registerDrawCallBack(block)
    }

    private fun requireLoopHandler() = _loopHandler ?: loopHandlerBuilder.build().also {
        _loopHandler = it
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
}

private class LoopHandlerBuilder(contextRegistry: ContextRegistry) {
    private var updateActions = mutableListOf<UpdateHandler>()
    private var drawActions = mutableListOf<DrawHandler>()
    private val gameContext = contextRegistry.get<GameContext>()
    private val drawContext = contextRegistry.get<DrawContext>()

    fun registerUpdateCallBack(block: GameContext.(deltaTime: Float) -> Unit) {
        updateActions.add(
            UpdateHandler { deltaTime ->
                block.invoke(gameContext, deltaTime)
            }
        )
    }

    fun registerDrawCallBack(block: DrawContext.() -> Unit) {
        drawActions.add(DrawHandler { block.invoke(drawContext) })
    }

    fun build(): LoopHandler = object : LoopHandler {
        override fun performUpdate(deltaTime: Float) {
            updateActions.forEach { it.performUpdate(deltaTime) }
        }

        override fun performDraw() {
            drawActions.forEach { it.performDraw() }
        }
    }
}


internal interface ComponentFactory : ComponentRegistry {
    fun buildComponents(block: ComponentRegistry.() -> Unit)
}

internal class ComponentsBuilder(
    private val contextRegistry: ContextRegistry,
    disposableRegistry: DisposableRegistry,
) : ComponentFactory {
    var activeStates = HashMap<Any, Component>()
    private var pendingStates = HashMap<Any, Component>()
    private val componentKeys = mutableSetOf<Any>()

    private val rememberScope = RememberScope(disposableRegistry, contextRegistry)
    private val rememberedStates = mutableListOf<Any?>()
    private var readIndex = 0

    override fun <R> remember(block: RememberScope.() -> R): R {
        if (readIndex < rememberedStates.size) {
            return rememberedStates[readIndex++] as R
        }
        val value = block(rememberScope)
        rememberedStates.add(value)
        readIndex++
        return value
    }

    override fun component(id: Any, block: ComponentScope.() -> Unit) {
        checkUniqueKey(id)

        val component = activeStates.remove(id) ?: Component(id, contextRegistry)
        component.buildComponents {
            component.block()
        }
        pendingStates[id] = component
    }

    override fun buildComponents(block: ComponentRegistry.() -> Unit) {
        this.apply(block)
        onEndBuildComponents()
    }

    private fun onEndBuildComponents() {
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
