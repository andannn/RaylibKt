package raylib.core

import kotlinx.cinterop.MemScope
import kotlinx.cinterop.alloc
import raylib.core.internal.DummyWindowFunction
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

class MutableStateTest {
    private lateinit var windowScope: DefaultWindowScope

    @BeforeTest
    fun setUp() {
        windowScope = DefaultWindowScope(
            windowFunction = DummyWindowFunction(),
            memScope = MemScope()
        )
    }

    @AfterTest
    fun close() {
        windowScope.dispose()
    }

    @Test
    fun mutableStateTest_invalid_required_when_set_value() {
        val state = windowScope.stateBox(0)
        assertFalse(windowScope.isDirty)
        state.value = 1
        assertTrue(windowScope.isDirty)
    }

    @Test
    fun managedStateListTest_build() = with(windowScope) {
        val list = stateList {
            addState(disposableState { "1" })
        }
        assertEquals(1, list.size)
    }

    @Test
    fun managedStateListTest_add(): Unit = with(windowScope) {
        val list = stateList {
            addState(disposableState { alloc<Vector2> {x = 1f} })
        }
        assertEquals(1, list.size)

        list.addState(disposableState { alloc<Vector2> {x = 2f} })
        assertEquals(2, list.size)
    }

    @Test
    fun managedStateListTest_remove(): Unit = with(windowScope) {
        val state1 = disposableState {
            alloc<Vector2>().apply { x = 1f }
        }
        val list = stateList {
            addState(state1)
            addState(disposableState {
                alloc<Vector2>()
            })
        }
        assertEquals(2, list.size)
        state1.dispose()
        prepareBuild()
        assertTrue(state1.isDisposed)
        assertEquals(1, list.size)
        // native placement scope is freed. this Native object point to invalid address.
        assertNotEquals(1f, state1.value.x)
    }
}