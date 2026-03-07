package raylib.core

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

interface ComponentRegistry {
    fun component(id: Any, block: ComponentScope.() -> Unit)
}

inline fun <reified R> ComponentRegistry.remember(block: RememberScope.() -> R): R {
    val builder = (this as StateStore)
    return builder.rememberedValue().let {
        if (it == null) {
            val value = block(rememberScope)
            updateRememberedValue(value)
            value
        } else it
    } as R
}

interface ComponentScope : WindowFunction, DisposableRegistry, ComponentRegistry, ContextProvider {
    fun onUpdate(block: OnUpdateListener)

    fun onDraw(block: OnDrawListener)

    fun setDrawInterceptor(interceptor: DrawInterceptor)
}

fun interface OnUpdateListener {
    fun GameContext.onUpdate(deltaTime: Float)
}

fun interface OnDrawListener {
    fun DrawContext.onDraw()
}

fun interface DrawHandler {
    fun performDraw()
}

fun interface DrawInterceptor {
    fun interceptDraw(handler: DrawHandler)
}

infix fun DrawInterceptor.then(next: DrawInterceptor): DrawInterceptor {
    if (this === NoOpDrawInterceptor) return next
    if (next === NoOpDrawInterceptor) return this
    return CompositeInterceptor(this, next)
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

internal object NoOpDrawInterceptor : DrawInterceptor {
    override fun interceptDraw(handler: DrawHandler) {
        return handler.performDraw()
    }
}

internal class CompositeInterceptor(
    private val outer: DrawInterceptor,
    private val inner: DrawInterceptor
) : DrawInterceptor {
    override fun interceptDraw(handler: DrawHandler) {
        outer.interceptDraw {
            inner.interceptDraw(handler)
        }
    }
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
    StateStore by componentsBuilder,
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

    private var _drawInterceptor: DrawInterceptor = NoOpDrawInterceptor

    private var isInterceptorLocked = false

    private val drawThis = DrawHandler {
        requireLoopHandler().performDraw()

        children.forEach {
            it.performDraw()
        }
    }

    override fun setDrawInterceptor(interceptor: DrawInterceptor) {
        if (!isInterceptorLocked) {
            _drawInterceptor = interceptor
            isInterceptorLocked = true
        }
    }

    override fun performUpdate(deltaTime: Float) {
        requireLoopHandler().performUpdate(deltaTime)

        children.forEach {
            it.performUpdate(deltaTime)
        }
    }

    override fun performDraw() {
        _drawInterceptor.interceptDraw(drawThis)
    }

    override fun onUpdate(block: OnUpdateListener) {
// TODO: rebuild _loopHandler
        if (_loopHandler != null) return
        loopHandlerBuilder.registerUpdateCallBack(block)
    }

    override fun onDraw(block: OnDrawListener) {
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

    fun registerUpdateCallBack(block: OnUpdateListener) {
        updateActions.add(
            UpdateHandler { deltaTime ->
                with(block) {
                    gameContext.onUpdate(deltaTime = deltaTime)
                }
            }
        )
    }

    fun registerDrawCallBack(block: OnDrawListener) {
        drawActions.add(
            DrawHandler {
                with(block) {
                    drawContext.onDraw()
                }
            }
        )
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

@PublishedApi
internal interface StateStore {
    fun rememberedValue(): Any?
    fun updateRememberedValue(value: Any?)
    val rememberScope: RememberScope
}

internal class ComponentsBuilder(
    private val contextRegistry: ContextRegistry,
    disposableRegistry: DisposableRegistry,
    override val rememberScope: RememberScope = RememberScope(disposableRegistry, contextRegistry)
) : ComponentFactory, StateStore {
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
