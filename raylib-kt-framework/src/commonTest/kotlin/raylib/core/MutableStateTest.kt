package raylib.core

import kotlinx.cinterop.alloc
import raylib.core.internal.DummyWindowFunction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MutableStateTest {
    private lateinit var windowScope: WindowContextImpl

    @BeforeTest
    fun setUp() {
        windowScope = WindowContextImpl(
            contextRegistry = ContextRegistryImpl(),
            windowFunction = DummyWindowFunction()
        )
    }

    @AfterTest
    fun close() {
        windowScope.dispose()
    }

    @Test
    fun managedStateListTest_build() = with(windowScope) {
        val list = mutableStateListOf(
            nativeStateOf { "1" }
        )
        assertEquals(1, list.size)
    }

    @Test
    fun managedStateListTest_add(): Unit = with(windowScope) {
        val list = mutableStateListOf(
            nativeStateOf { alloc<Vector2> { x = 1f } }
        )
        assertEquals(1, list.size)

        list.addState(nativeStateOf { alloc<Vector2> { x = 2f } })
        assertEquals(2, list.size)
    }

    @Test
    fun managedStateListTest_remove(): Unit = with(windowScope) {
        val state1 = nativeStateOf {
            alloc<Vector2>().apply { x = 1f }
        }
        val list = mutableStateListOf(
            state1,
            nativeStateOf {
                alloc<Vector2>()
            }
        )
        assertEquals(2, list.size)
        state1.dispose()
        assertEquals(1f, state1.value.x)

        onFrame()

        assertTrue(state1.isDisposed)
        assertTrue(state1.isFreed)
        assertEquals(1, list.size)
        // native placement scope is freed. this Native object point to invalid address.
        assertNotEquals(1f, state1.value.x)
    }

    @Test
    fun disposableStateTest_dispose(): Unit = with(windowScope) {
        val state = nativeStateOf {
            alloc<Vector2> { x = 100f }
        }
        assertEquals(100f, state.value.x)
        state.dispose()

        assertTrue(state.isDisposed)
        assertTrue(state.isFreed)
    }

    @Test
    fun disposableStateTest_disposed_when_container_dispose() = with(windowScope) {
        val state = nativeStateOf { alloc<Vector2> { x = 100f } }
        assertEquals(100f, state.value.x)

        this.dispose()

        assertTrue(state.isDisposed)
        assertTrue(state.isFreed)
    }
}