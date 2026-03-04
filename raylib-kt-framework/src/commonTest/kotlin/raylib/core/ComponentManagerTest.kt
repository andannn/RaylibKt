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
        val manager = buildComponentManager(
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
        assertEquals(0, manager.components.size)

        manager.buildComponents()
        assertEquals(listOf("component1", "component2"), manager.components.map { it.componentId })

        isAddComponent = true
        manager.buildComponents()
        assertEquals(3, manager.components.size)
        assertEquals(listOf("component1", "component2", "component3"), manager.components.map { it.componentId })
    }

    @Test
    fun buildComponent_remove() {
        var isRemoveComponent = false
        val manager = buildComponentManager(
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
        assertEquals(0, manager.components.size)

        manager.buildComponents()
        assertEquals(listOf("component1", "component2", "component3"), manager.components.map { it.componentId })

        isRemoveComponent = true
        manager.buildComponents()
        assertEquals(listOf("component1", "component3"), manager.components.map { it.componentId })
    }

    @Test
    fun buildComponent_same_component_will_not_be_initialized_again() {
        var isRemoveComponent = false
        var called = 0
        val manager = buildComponentManager(
            {
                component("component1") {
                    called++
                }
                if (!isRemoveComponent) {
                    component("component2") {
                    }
                }
            }
        )

        manager.buildComponents()
        assertEquals(1, called)

        isRemoveComponent = true
        manager.buildComponents()
        assertEquals(1, called)

    }

    @Test
    fun buildComponent_duplicate_component_will_throw_exception() {
        val manager = buildComponentManager(
            {
                component("component1") {
                }
                component("component1") {
                }
            }
        )
        assertFails("Duplicate component key detected -> 'component1'. Each component in the same scope must have a unique ID.") {
            manager.buildComponents()
        }
    }

    @Test
    fun buildComponent_component_is_disposed_when_manager_is_disposed() {
        var called = false
        val manager = buildComponentManager(
            block = {
                component("component1") {
                    disposeOnClose {
                        called = true
                    }
                }
            }
        )
        manager.buildComponents()
        assertFalse(called)
        manager.dispose()
        assertTrue(called)
    }

    @Test
    fun buildComponent_component_is_disposed_not_included_in_rebuild() {
        var called = 0
        var isAddComponent = true
        val manager = buildComponentManager(
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
        manager.buildComponents()
        assertEquals(0, called)


        isAddComponent = false
        manager.buildComponents()

        assertEquals(1, called)
    }

    @Test
    fun buildComponent_remember_state_component() {
        var currentValue: MutableState<String>? = null
        val manager = buildComponentManager(
            block = {
                val value = remember("value 1") {
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

        manager.buildComponents()
        assertEquals("A", currentValue?.value)


        manager.buildComponents()
        manager.performUpdate(2f)
        assertEquals("B", currentValue?.value)


        manager.buildComponents()
        assertEquals("B", currentValue?.value)

    }

    @Test
    fun buildComponent_remembered_state_is_disposed_component_disposed() {
        var value: DisposableState<Vector2>? = null
        var isValue = true
        val manager = buildComponentManager(
            block = {
                if (isValue) {
                    value = remember("value 1") {
                        nativeStateOf { alloc<Vector2> { x = 100f } }
                    }
                }
            }
        )

        manager.buildComponents()
        assertEquals(100f, value?.value?.x)

        isValue = false
        manager.buildComponents()

        assertEquals(true, value?.isDisposed)
    }
}

private fun buildComponentManager(block: ComponentRegistry.() -> Unit) = ComponentRegistryImpl(
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
