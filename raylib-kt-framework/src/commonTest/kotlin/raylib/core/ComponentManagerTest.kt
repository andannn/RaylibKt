package raylib.core

import kotlinx.cinterop.alloc
import raylib.core.internal.DummyWindowFunction
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ComponentManagerTest {

    @Test
    fun buildComponent_add() {
        var isAddComponent = false
        val rootComponent = rootComponent(
            block = {
                component("component1") {
                }
                component("component2") {
                }
                if (isAddComponent) {
                    component("component3") {
                    }
                }
            }
        )
        assertEquals(0, rootComponent.children.toList().size)

        rootComponent.buildComponents()
        assertEquals(listOf("component1", "component2"), rootComponent.children.toList().map { it.componentId })

        isAddComponent = true
        rootComponent.buildComponents()
        assertEquals(3, rootComponent.children.toList().size)
        assertEquals(listOf("component1", "component2", "component3"), rootComponent.children.toList().map { it.componentId })
    }

    @Test
    fun buildComponent_remove() {
        var isRemoveComponent = false
        val rootComponent = rootComponent(
            {
                component("component1") {
                }
                if (!isRemoveComponent) {
                    component("component2") {
                    }
                }
                component("component3") {
                }
            }
        )
        assertEquals(0, rootComponent.children.toList().size)

        rootComponent.buildComponents()
        assertEquals(listOf("component1", "component2", "component3"), rootComponent.children.toList().map { it.componentId })

        isRemoveComponent = true
        rootComponent.buildComponents()
        assertEquals(listOf("component1", "component3"), rootComponent.children.toList().map { it.componentId })
    }

    @Test
    fun buildComponent_duplicate_component_will_throw_exception() {
        val rootComponent = rootComponent(
            {
                component("component1") {
                }
                component("component1") {
                }
            }
        )
        assertFails("Duplicate component key detected -> 'component1'. Each component in the same scope must have a unique ID.") {
            rootComponent.buildComponents()
        }
    }

    @Test
    fun buildComponent_component_is_disposed_when_manager_is_disposed() {
        var called = false
        val rootComponent = rootComponent(
            block = {
                component("component1") {
                    disposeOnClose {
                        called = true
                    }
                }
            }
        )
        rootComponent.buildComponents()
        assertFalse(called)
        rootComponent.dispose()
        assertTrue(called)
    }

    @Test
    fun buildComponent_component_is_disposed_not_included_in_rebuild() {
        var called = 0
        var isAddComponent = true
        val rootComponent = rootComponent(
            block = {
                if (isAddComponent) {
                    component("component1") {
                        disposeOnClose {
                            called++
                        }
                    }
                }
                component("component2") {
                }
            }
        )
        rootComponent.buildComponents()
        assertEquals(0, called)


        isAddComponent = false
        rootComponent.buildComponents()

        assertEquals(1, called)
    }

    @Test
    fun buildComponent_remember_state_component() {
        var currentValue: MutableState<String>? = null
        val rootComponent = rootComponent(
            block = {
                val value = remember {
                    mutableStateOf("A")
                }
                currentValue = value
                component("component2") {
                    onUpdate {
                        value.value = "B"
                    }
                }
            }
        )

        rootComponent.buildComponents()
        assertEquals("A", currentValue?.value)

        rootComponent.buildComponents()
        rootComponent.performUpdate(2f)
        assertEquals("B", currentValue?.value)

        rootComponent.buildComponents()
        assertEquals("B", currentValue?.value)
    }
}

private fun rootComponent(block: ComponentRegistry.() -> Unit) = RootComponent(
    contextRegistry = ContextRegistryImpl().apply {
        val windowContext = WindowContextImpl(
            windowFunction = DummyWindowFunction()
        )
        val gameContext = GameContext(windowContext)
        val drawContext = DrawContext(windowContext)
        put<WindowContext>(windowContext)
        put(gameContext)
        put(drawContext)
    },
    block = block
)
