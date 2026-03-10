package io.github.andannn.raylib.components

import io.github.andannn.raylib.base.Rectangle
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.core.internal.buildComponents
import io.github.andannn.raylib.core.provideStaticDependency
import io.github.andannn.raylib.core.remember
import kotlin.test.Test
import kotlin.test.assertEquals

class CollisionCollectionContextTest {
    @Test
    fun spatialCollection_query() {
        val collection = CollisionCollectionContext(50)
        var state: Any? = null
        val control = buildComponents(
            init = { provideStaticDependency(collection) }
        ) {
            state = remember {
                Spatial2DBoxStateAlloc(position = Vector2(40f, 40f))
            }
            box2DComponent(
                state,
                Vector2(20f, 20f)
            ) {

            }
        }

        control.rebuild()

        assertEquals(state, collection.queryIn(Rectangle(width = 50f, height = 50f)).first())
        assertEquals(state, collection.queryIn(Rectangle(0f, 50f, width = 50f, height = 50f)).first())
        assertEquals(state, collection.queryIn(Rectangle(50f, 0f, width = 50f, height = 50f)).first())
        assertEquals(state, collection.queryIn(Rectangle(50f, 50f, width = 50f, height = 50f)).first())
    }
}