package raylib.core

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

@MustUseReturnValues
interface ComponentScope : DisposableRegistry, WindowFunction, ContextProvider {
    fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit)

    fun onDraw(block: DrawContext.() -> Unit)
}


fun interface UpdateHandler {
    fun update(deltaTime: Float)
}

fun interface DrawHandler {
    fun draw()
}

internal interface LoopHandler : UpdateHandler, DrawHandler

internal fun Component(
    id: Any,
    contextRegistry: ContextRegistry,
): AbstractComponent = object : AbstractComponent(id, contextRegistry) {}

internal abstract class AbstractComponent(
    val componentId: Any,
    private val contextRegistry: ContextRegistry,
) : ComponentScope, ContextRegistry by contextRegistry, WindowFunction by contextRegistry.get<WindowContext>(),
    Disposable {

    private var loopHandler: LoopHandler? = null
    private val builder = LoopHandlerBuilder(contextRegistry)
    fun requireLoopHandler() = loopHandler ?: builder.build().also {
        loopHandler = it
    }

    @OptIn(ExperimentalNativeApi::class)
    private val cleaner = createCleaner(componentId) {
        println("Runtime Monitor: Component [$it] has been Garbage Collected.")
    }

    private val disposableRegistry = DisposableRegistryImpl()

    override fun disposeOnClose(disposable: Disposable) = disposableRegistry.disposeOnClose(disposable)

    override fun dispose() {
        disposableRegistry.dispose()
    }

    override fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit) {
        builder.onUpdate(block)
    }

    override fun onDraw(block: DrawContext.() -> Unit) {
        builder.onDraw(block)
    }

    private class LoopHandlerBuilder(contextRegistry: ContextRegistry) {
        private var updateActions = mutableListOf<UpdateHandler>()
        private var drawActions = mutableListOf<DrawHandler>()
        private val gameContext = contextRegistry.get<GameContext>()
        private val drawContext = contextRegistry.get<DrawContext>()

        fun onUpdate(block: GameContext.(deltaTime: Float) -> Unit) {
            updateActions.add(UpdateHandler { deltaTime ->
                block.invoke(gameContext, deltaTime)
            })
        }

        fun onDraw(block: DrawContext.() -> Unit) {
            drawActions.add(DrawHandler { block.invoke(drawContext) })
        }

        fun build(): LoopHandler = object : LoopHandler {
            override fun update(deltaTime: Float) {
                updateActions.forEach { it.update(deltaTime) }
            }

            override fun draw() {
                drawActions.forEach { it.draw() }
            }
        }
    }
}

internal class StateComponent<R>(
    componentId: Any,
    contextRegistry: ContextRegistry,
) : AbstractComponent(
    componentId,
    contextRegistry,
) {
    var internalValue: R? = null
    val value: R
        get() = internalValue ?: error("value not init")
}

internal  fun <R> AbstractComponent.value() = (this as StateComponent<*>).value as R