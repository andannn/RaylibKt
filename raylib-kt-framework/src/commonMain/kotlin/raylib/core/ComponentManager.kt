package raylib.core

interface ComponentRegistry {
    fun <R> remember(id: Any, block: ComponentScope.() -> R): R
    fun component(id: Any, block: ComponentScope.() -> Unit)
}

internal interface ComponentManager : ComponentRegistry, Disposable {
    fun performUpdate(deltaTime: Float)
    fun buildComponents()
    fun performDraw()
}

internal class ComponentRegistryImpl(
    contextRegistry: ContextRegistry,
    private val block: ComponentRegistry.() -> Unit,
    private val componentsBuilder: ComponentsBuilder = ComponentsBuilder(contextRegistry)
) : ComponentManager, ComponentRegistry by componentsBuilder {
    internal val components
        get() = componentsBuilder.activeStates.values

    override fun buildComponents() {
        componentsBuilder.apply(block)
        componentsBuilder.endBuild()
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
    }
}

internal class ComponentsBuilder(
    private val contextRegistry: ContextRegistry
) : ComponentRegistry {
    var activeStates = HashMap<Any, AbstractComponent>()
    var pendingStates = HashMap<Any, AbstractComponent>()
    private val componentKeys = mutableSetOf<Any>()

    override fun <R> remember(id: Any, block: ComponentScope.() -> R): R {
        require(componentKeys.add(id)) {
            "Error: Duplicate component key detected -> '$id'. " +
                    "Each component in the same scope must have a unique ID."
        }
        val component = activeStates.remove(id) ?: StateComponent<R>(id, contextRegistry).apply {
            internalValue = block()
        }
        pendingStates[id] = component
        return component.value()
    }

    override fun component(id: Any, block: ComponentScope.() -> Unit) {
        require(componentKeys.add(id)) {
            "Error: Duplicate component key detected -> '$id'. " +
                    "Each component in the same scope must have a unique ID."
        }

        val component = activeStates.remove(id) ?: Component(id, contextRegistry).apply(block)
        pendingStates[id] = component
    }

    fun endBuild() {
        componentKeys.clear()

        if (activeStates.isNotEmpty()) {
            activeStates.values.forEach {
                println("Component [${it.componentId}] has been disposed.")
                it.dispose()
            }
            activeStates.clear()
        }

        val temp = activeStates
        activeStates = pendingStates
        pendingStates = temp
    }
}
