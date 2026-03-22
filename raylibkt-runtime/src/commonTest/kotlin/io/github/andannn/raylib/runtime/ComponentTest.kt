/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.runtime

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ComponentTest {

    @Test
    fun buildComponent_add() {
        var isAddComponent = false
        val rootComponent = Rebuildable(
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

        rootComponent.rebuild()
        assertEquals(listOf("component1", "component2"), rootComponent.children.toList().map { it.componentId })

        isAddComponent = true
        rootComponent.rebuild()
        assertEquals(3, rootComponent.children.toList().size)
        assertEquals(
            listOf("component1", "component2", "component3"),
            rootComponent.children.toList().map { it.componentId })
    }

    @Test
    fun buildComponent_remove() {
        var isRemoveComponent = false
        val rootComponent = Rebuildable {
            component("component1") {
            }
            if (!isRemoveComponent) {
                component("component2") {
                }
            }
            component("component3") {
            }
        }
        assertEquals(0, rootComponent.children.toList().size)

        rootComponent.rebuild()
        assertEquals(
            listOf("component1", "component2", "component3"),
            rootComponent.children.toList().map { it.componentId })

        isRemoveComponent = true
        rootComponent.rebuild()
        assertEquals(listOf("component1", "component3"), rootComponent.children.toList().map { it.componentId })
    }

    @Test
    fun buildComponent_duplicate_component_will_throw_exception() {
        val rootComponent = Rebuildable {
            component("component1") {
            }
            component("component1") {
            }
        }
        assertFails("Duplicate component key detected -> 'component1'. Each component in the same scope must have a unique ID.") {
            rootComponent.rebuild()
        }
    }

    @Test
    fun buildComponent_component_is_disposed_when_manager_is_disposed() {
        var called = false
        val rootComponent = Rebuildable(
            block = {
                component("component1") {
                    disposeOnClose {
                        called = true
                    }
                }
            }
        )
        rootComponent.rebuild()
        assertFalse(called)
        rootComponent.dispose()
        assertTrue(called)
    }

    @Test
    fun buildComponent_component_is_disposed_not_included_in_rebuild() {
        var called = 0
        var isAddComponent = true
        val rootComponent = Rebuildable(
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
        rootComponent.rebuild()
        assertEquals(0, called)


        isAddComponent = false
        rootComponent.rebuild()

        assertEquals(1, called)
    }

    @Test
    fun buildComponent_child_component_disposed_with_parent() {
        var disposed = false
        var show = true
        val rootComponent = Rebuildable(
            block = {
                if (show) {
                    component("parent") {
                        component("child") {
                            disposeOnClose {
                                disposed = true
                            }
                        }
                    }
                }
            }
        )
        rootComponent.rebuild()
        assertFalse(disposed)
        show = false
        rootComponent.rebuild()
        assertTrue(disposed)
    }
}

private val Rebuildable.children
    get() = (this as Component).children