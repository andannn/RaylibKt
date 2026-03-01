package raylib.core

import kotlin.experimental.ExperimentalNativeApi
import kotlin.native.ref.createCleaner

internal fun Component(
    componentId: Any,
    handler: LoopHandler,
    scope: Disposable
): Component = object : Component(componentId), LoopHandler by handler, Disposable by scope {}

internal abstract class Component(val componentId: Any) : LoopHandler, Disposable {
    @OptIn(ExperimentalNativeApi::class)
    val cleaner = createCleaner(componentId) {
        println("Runtime Monitor: Component [$it] has been Garbage Collected.")
    }
}

@MustUseReturnValues
class ComponentScope(
    private val windowFunction: WindowFunction,
) : DisposableRegistry, WindowFunction by windowFunction {
    private val disposableRegistry = DisposableRegistryImpl()

    fun provideHandlers(
        block: LoopHandlerBuilder.() -> Unit,
    ): LoopHandler {
        return LoopHandlerBuilder(windowFunction).apply(block).build()
    }

    override fun disposeOnClose(disposable: Disposable) = disposableRegistry.disposeOnClose(disposable)

    internal fun dispose() {
        disposableRegistry.dispose()
    }
}
