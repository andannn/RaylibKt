package raylib.core

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
        var isDirty = false
        val manager = buildComponentManager(
            isDirty = { isDirty },
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
        manager.buildComponentsIfNeeded()
        assertEquals(listOf("component1", "component2"), manager.components.map { it.componentId })

        isDirty = true
        isAddComponent = true
        manager.buildComponentsIfNeeded()
        assertEquals(3, manager.components.size)
        assertEquals(listOf("component1", "component2", "component3"), manager.components.map { it.componentId })
    }

    @Test
    fun buildComponent_remove() {
        var isRemoveComponent = false
        var isDirty = false
        val manager = buildComponentManager(
            isDirty = { isDirty },
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
        manager.buildComponentsIfNeeded()
        assertEquals(listOf("component1", "component2", "component3"), manager.components.map { it.componentId })

        isDirty = true
        isRemoveComponent = true
        manager.buildComponentsIfNeeded()
        assertEquals(listOf("component1", "component3"), manager.components.map { it.componentId })
    }

    @Test
    fun buildComponent_same_component_will_not_be_initialized_again() {
        var isRemoveComponent = false
        var isDirty = false
        var called = 0
        val manager = buildComponentManager(
            isDirty = { isDirty },
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
        manager.buildComponentsIfNeeded()
        assertEquals(1, called)
        isDirty = true
        isRemoveComponent = true
        manager.buildComponentsIfNeeded()
        assertEquals(1, called)
    }

    @Test
    fun buildComponent_duplicate_component_will_throw_exception() {
        val manager = buildComponentManager(
            isDirty = { false },
            {
                component("component1") {
                }
                component("component1") {
                }
            }
        )
        assertFails("Duplicate component key detected -> 'component1'. Each component in the same scope must have a unique ID.") {
            manager.buildComponentsIfNeeded()
        }
    }

    @Test
    fun buildComponent_component_is_disposed_when_manager_is_disposed() {
        var called = false
        val manager = buildComponentManager(
            isDirty = { false },
            block = {
                component("component1") {
                    disposeOnClose {
                        called = true
                    }
                }
            }
        )
        manager.buildComponentsIfNeeded()
        assertFalse(called)
        manager.dispose()
        assertTrue(called)
    }

    @Test
    fun buildComponent_component_is_disposed_not_included_in_rebuild() {
        var called = 0
        var isDirty = false
        var isAddComponent = true
        val manager = buildComponentManager(
            isDirty = { isDirty },
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
        manager.buildComponentsIfNeeded()
        assertEquals(0, called)

        isDirty = true
        isAddComponent = false
        manager.buildComponentsIfNeeded()
        assertEquals(1, called)
    }

}

private fun buildComponentManager(isDirty: () -> Boolean, block: ComponentFactory.() -> Unit) = ComponentManagerImpl(
    contextRegistry = ContextRegistryImpl().apply {
        val windowContext = WindowContextImpl(
            contextRegistry = this,
            windowFunction = DummyWindowFunction()
        )
        val gameContext = GameContext(windowContext)
        val drawContext = DrawContext(windowContext)
        put<WindowContext>(windowContext)
        put(gameContext)
        put(drawContext)
    },
    isDirty = isDirty,
    onRebuildFinished = {},
    block = block
)
