package raylib.core

fun interface Disposable {
    fun dispose()
}

interface DisposableRegistry {
    fun disposeOnClose(disposable: Disposable)
}

internal class DisposableRegistryImpl : DisposableRegistry, Disposable {
    private val disposables = mutableListOf<Disposable>()

    override fun disposeOnClose(disposable: Disposable) {
        disposables.add(disposable)
    }

    override fun dispose() {
        disposables.forEach { it.dispose() }
        disposables.clear()
    }
}