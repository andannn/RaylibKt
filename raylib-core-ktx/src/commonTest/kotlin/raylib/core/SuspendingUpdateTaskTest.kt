package raylib.core

import raylib.core.internal.DummyWindowFunction
import kotlin.test.Test
import kotlin.test.assertEquals

class SuspendingUpdateTaskTest {
    private val gameContext = GameContext(DummyWindowFunction())

    @Test
    fun suspendingTask_resume_on_update() {
        var count = 0
        val task = SuspendingUpdateTask(gameContext) {
            awaitUpdateEventScope {
                count++
                awaitUpdateEvent()
                count++
                awaitUpdateEvent()
                count++
                awaitUpdateEvent()
            }
        }
        assertEquals(0, count)
        task.start()
        assertEquals(1, count)
        task.update(gameContext, 1f)
        assertEquals(2, count)
        task.update(gameContext, 1f)
        assertEquals(3, count)
    }

    @Test
    fun suspendingTask_task_stop() {
        var count = 0
        val task = SuspendingUpdateTask(gameContext) {
            awaitUpdateEventScope {
                count++
                awaitUpdateEvent()
                count++
                awaitUpdateEvent()
            }
        }
        assertEquals(0, count)
        task.start()
        assertEquals(1, count)
        task.stop()
        task.update(gameContext, 1f)
        assertEquals(1, count)
    }

    @Test
    fun suspendingTask_update_event_trigger_in_same_frame() {
        var count = 0
        val task = SuspendingUpdateTask(gameContext) {
            awaitUpdateEventScope {
                count++
                awaitUpdateEvent()
                count++
            }
            awaitUpdateEventScope {
                count++
                awaitUpdateEvent()
                count++
            }
        }
        assertEquals(0, count)
        task.start()
        assertEquals(1, count)
        task.update(gameContext, 1f)
        assertEquals(4, count)
    }
}