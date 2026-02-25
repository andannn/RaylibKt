package raylib.core

import raylib.core.internal.DummyWindowFunction
import raylib.core.internal.LeakDetector
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
        val manager = ComponentManagerImpl(
            isDirty = { isDirty },
            windowFunction = DummyWindowFunction(),
            onRebuildFinished = {},
            block = {
                component("component1") {
                    provideHandlers {}
                }
                component("component2") {
                    provideHandlers {}
                }
                if (isAddComponent) {
                    component("component3") {
                        provideHandlers {}
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
        val manager = ComponentManagerImpl(
            windowFunction = DummyWindowFunction(),
            isDirty = { isDirty },
            onRebuildFinished = {},
            {
                component("component1") {
                    provideHandlers {}
                }
                if (!isRemoveComponent) {
                    component("component2") {
                        provideHandlers {}
                    }
                }
                component("component3") {
                    provideHandlers {}
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
        val manager = ComponentManagerImpl(
            windowFunction = DummyWindowFunction(),
            isDirty = { isDirty },
            onRebuildFinished = {},
            {
                component("component1") {
                    called++
                    provideHandlers {}
                }
                if (!isRemoveComponent) {
                    component("component2") {
                        provideHandlers {}
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
        val manager = ComponentManagerImpl(
            windowFunction = DummyWindowFunction(),
            isDirty = { false },
            onRebuildFinished = {},
            {
                component("component1") {
                    provideHandlers {}
                }
                component("component1") {
                    provideHandlers {}
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
        val manager = ComponentManagerImpl(
            windowFunction = DummyWindowFunction(),
            isDirty = { false },
            onRebuildFinished = {},
            block = {
                component("component1") {
                    disposeOnClose {
                        called = true
                    }
                    provideHandlers {}
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
        val manager = ComponentManagerImpl(
            windowFunction = DummyWindowFunction(),
            isDirty = { isDirty },
            onRebuildFinished = {},
            block = {
                if (isAddComponent) {
                    component("component1") {
                        disposeOnClose {
                            called++
                        }
                        provideHandlers {}
                    }
                }
                component("component2") {
                    provideHandlers {}
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