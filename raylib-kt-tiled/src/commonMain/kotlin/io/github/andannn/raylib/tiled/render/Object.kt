/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.tiled.render

import io.github.andannn.raylib.base.Colors.RED
import io.github.andannn.raylib.base.Vector2
import io.github.andannn.raylib.components.Spatial2DAlloc
import io.github.andannn.raylib.components.Transform2DAlloc
import io.github.andannn.raylib.components.spatial2DComponent
import io.github.andannn.raylib.components.transform2DComponent
import io.github.andannn.raylib.core.ComponentScope
import io.github.andannn.raylib.core.mutableStateOf
import io.github.andannn.raylib.core.draw
import io.github.andannn.raylib.core.remember
import io.github.andannn.raylib.tiled.model.*

inline fun ComponentScope.bindObjects(
    objects: List<Object>,
    crossinline onBindObject: ComponentScope.(Object) -> Unit
) {
// TODO: handle draw order.
    objects.forEach { obj ->
        when (obj) {
            is RectObject -> bindRect(obj, onBindObject)
            is PointObject -> bindPoint(obj, onBindObject)
            is TemplateObject -> error("Never")
            is TileObject -> TODO("implement TileObject")
            is CapsuleObject -> TODO("implement CapsuleObject")
            is EllipseObject -> TODO("implement EllipseObject")
            is PolygonObject -> TODO("implement PolygonObject")
            is PolylineObject -> TODO("implement PolylineObject")
            is TextObject -> TODO("implement TextObject")
        }
    }
}

inline fun ComponentScope.bindRect(
    obj: RectObject,
    crossinline onBindObject: ComponentScope.(Object) -> Unit
) {
    val spatial2D = remember {
        Spatial2DAlloc(
            size = Vector2(obj.width.toFloat(), obj.height.toFloat()),
            position = Vector2(obj.requireX().toFloat(), obj.requireY().toFloat()),
            angle = mutableStateOf(obj.rotation.toFloat())
        )
    }

    spatial2DComponent(obj.id, state = spatial2D) {
        onBindObject(obj)

        if (isDebug) {
            draw {
                drawText(obj.name, 0, -12, 10, RED)
            }
        }

    }
}

inline fun ComponentScope.bindPoint(
    obj: PointObject,
    crossinline onBindObject: ComponentScope.(Object) -> Unit
) {
    val transform = remember {
        Transform2DAlloc(
            position = Vector2(obj.requireX().toFloat(), obj.requireY().toFloat()),
        )
    }
    transform2DComponent(obj.id, transform) {
        onBindObject(obj)

        if (isDebug) {
            draw {
                drawText(obj.name, 0, -10, 10, RED)
            }
        }
    }
}
