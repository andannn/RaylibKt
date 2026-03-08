package raylib.core

import kotlinx.cinterop.alloc
import raylib.core.internal.DummyWindowContextImpl
import raylib.core.internal.DummyWindowFunction
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
            DisposableRegistry by disposableRegistry,
            WindowContext by DummyWindowContextImpl() {}
    }

    @Test
    fun managedStateListTest_build() = with(rememberScope) {
        val list = mutableStateListOf<String>()
        list.addState { nativeStateOf { "1" } }
        assertEquals(1, list.size)
    }

    @Test
    fun managedStateListTest_add(): Unit = with(rememberScope) {
        val list = mutableStateListOf<Vector2>()
        list.addState {
            nativeStateOf { alloc<Vector2> { x = 1f } }
        }
        assertEquals(1, list.size)

        list.addState { nativeStateOf { alloc<Vector2> { x = 2f } } }
        assertEquals(2, list.size)
    }

    @Test
    fun managedStateListTest_remove(): Unit = with(rememberScope) {
        val state1 = nativeStateOf {
            alloc<Vector2>().apply { x = 1f }
        }
        val list = mutableStateListOf<Vector2>()
        list.addState { state1 }

        list.addState {
            nativeStateOf {
                alloc<Vector2>()
            }
        }
        assertEquals(2, list.size)
        state1.dispose()

        // native placement scope is freed. this Native object point to invalid address.
        assertNotEquals(1f, state1.value.x)
        assertTrue(state1.isDisposed)
        assertTrue(state1.isFreed)

        assertEquals(1, list.size)
    }

    @Test
    fun disposableStateTest_dispose(): Unit = with(rememberScope) {
        val state = nativeStateOf {
            alloc<Vector2> { x = 100f }
        }
        assertEquals(100f, state.value.x)
        state.dispose()

        assertTrue(state.isDisposed)
        assertTrue(state.isFreed)
    }

    @Test
    fun disposableStateTest_disposed_when_container_dispose() = with(rememberScope) {
        val state = nativeStateOf { alloc<Vector2> { x = 100f } }
        assertEquals(100f, state.value.x)

        (this as Disposable).dispose()

        assertTrue(state.isDisposed)
        assertTrue(state.isFreed)
    }
}