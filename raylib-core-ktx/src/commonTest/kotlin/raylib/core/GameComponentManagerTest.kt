package raylib.core

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails

class GameComponentManagerTest {

    @Test
    fun buildGameComponent_add() {
        var isAddComponent = false
        var isDirty = false
        val manager = GameComponentManagerImpl(
            isDirty = { isDirty },
            onRebuildFinished = {},
            {
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
        assertEquals(0, manager.gameComponents.size)
        manager.buildComponentsIfNeeded()
        assertEquals(listOf("component1", "component2"), manager.gameComponents.map { it.componentId })

        isDirty = true
        isAddComponent = true
        manager.buildComponentsIfNeeded()
        assertEquals(3, manager.gameComponents.size)
        assertEquals(listOf("component1", "component2", "component3"), manager.gameComponents.map { it.componentId })
    }

    @Test
    fun buildGameComponent_remove() {
        var isRemoveComponent = false
        var isDirty = false
        val manager = GameComponentManagerImpl(
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
        assertEquals(0, manager.gameComponents.size)
        manager.buildComponentsIfNeeded()
        assertEquals(listOf("component1", "component2", "component3"), manager.gameComponents.map { it.componentId })

        isDirty = true
        isRemoveComponent = true
        manager.buildComponentsIfNeeded()
        assertEquals(listOf("component1", "component3"), manager.gameComponents.map { it.componentId })
    }

    @Test
    fun buildGameComponent_same_component_will_not_build_again() {
        var isRemoveComponent = false
        var isDirty = false
        var called = 0
        val manager = GameComponentManagerImpl(
            isDirty = { isDirty },
            onRebuildFinished = {},
            {
                component("component1") {
                    called ++
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
        manager.buildComponentsIfNeeded()
        assertEquals(1, called)
        isDirty = true
        isRemoveComponent = true
        manager.buildComponentsIfNeeded()
        assertEquals(1, called)
    }

    @Test
    fun buildGameComponent_duplicate_component_will_throw_exception() {
        val manager = GameComponentManagerImpl(
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
}