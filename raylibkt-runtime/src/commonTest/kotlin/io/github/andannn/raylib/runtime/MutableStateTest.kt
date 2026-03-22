/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.runtime

import kotlinx.cinterop.alloc
import kotlinx.cinterop.FloatVar
import kotlinx.cinterop.value
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MutableStateTest {
    private lateinit var rememberScope: RememberScope

    @BeforeTest
    fun setUp() {
        val disposableRegistry = DisposableRegistryImpl()
        rememberScope = object : RememberScope,
            Disposable by disposableRegistry,
            DisposableRegistry by disposableRegistry {}
    }

    @Test
    fun managedStateListTest_build() = with(rememberScope) {
        val list = mutableStateListOf<String>()
        list.addState { "1" }
        assertEquals(1, list.size)
    }

    @Test
    fun managedStateListTest_add(): Unit = with(rememberScope) {
        val list = mutableStateListOf<FloatVar>()
        list.addState {
            alloc<FloatVar> { value = 1f }
        }
        assertEquals(1, list.size)

        list.addState { alloc<FloatVar> {value = 2f } }
        assertEquals(2, list.size)
    }

    @Test
    fun managedStateListTest_remove(): Unit = with(rememberScope) {
        val list = mutableStateListOf<FloatVar>()
        val state1 = list.addState {
            alloc<FloatVar>().apply { value = 1f }
        }

        list.addState {
            alloc<FloatVar>()
        }
        assertEquals(2, list.size)
        state1.dispose()

        // native placement scope is freed. this Native object point to invalid address.
        assertNotEquals(1f, state1.value.value)
        assertTrue(state1.isDisposed)
        assertTrue(state1.isFreed)

        assertEquals(1, list.size)
    }

    @Test
    fun disposableStateTest_dispose(): Unit = with(rememberScope) {
        val state = nativeStateOf {
            alloc<FloatVar> { value = 100f }
        }
        assertEquals(100f, state.value.value)
        state.dispose()

        assertTrue(state.isDisposed)
        assertTrue(state.isFreed)
    }

    @Test
    fun disposableStateTest_disposed_when_container_dispose() = with(rememberScope) {
        val state = nativeStateOf { alloc<FloatVar> { value = 100f } }
        assertEquals(100f, state.value.value)

        (this as Disposable).dispose()

        assertTrue(state.isDisposed)
        assertTrue(state.isFreed)
    }

    @Test
    fun disposableStateTest_all_state_disposed_when_managed_list_dispose() = with(rememberScope) {
        val list = mutableStateListOf<FloatVar>()
        val state1 = list.addState { alloc<FloatVar> {} }
        val state2 = list.addState { alloc<FloatVar> {} }
        val state3 = list.addState { alloc<FloatVar> {} }
        list.dispose()

        assertTrue(state1.isDisposed)
        assertTrue(state2.isDisposed)
        assertTrue(state3.isDisposed)
    }
}