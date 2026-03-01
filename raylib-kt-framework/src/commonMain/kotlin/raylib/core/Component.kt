package raylib.core

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner


@MustUseReturnValues
interface ComponentScope : DisposableRegistry, WindowFunction {
    fun provideHandlers(
        block: LoopHandlerBuilder.() -> Unit,
    ): LoopHandler
}

internal class Component(
    val componentId: Any,
    private val contextRegistry: ContextRegistry,
) : ComponentScope, WindowFunction by contextRegistry.get<WindowContext>(), Disposable {

    var loopHandler: LoopHandler? = null
    fun requireLoopHandler() = loopHandler ?: error("LoopHandler is not initialized")

    @OptIn(ExperimentalNativeApi::class)
    private val cleaner = createCleaner(componentId) {
        println("Runtime Monitor: Component [$it] has been Garbage Collected.")
    }

    private val disposableRegistry = DisposableRegistryImpl()

    override fun provideHandlers(
        block: LoopHandlerBuilder.() -> Unit,
    ): LoopHandler {
        return LoopHandlerBuilder(contextRegistry).apply(block).build()
    }

    override fun disposeOnClose(disposable: Disposable) = disposableRegistry.disposeOnClose(disposable)

    override fun dispose() {
        disposableRegistry.dispose()
    }
}
