package me.raylib.sample.custom.game

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.core.Colors
import raylib.core.ComponentRegistry
import raylib.core.Rectangle
import raylib.core.Texture
import raylib.core.Vector2
import raylib.core.component
import raylib.core.components.SpriteGrid
import raylib.core.components.spriteAnimationComponent
import raylib.core.find
import raylib.core.getValue
import raylib.core.isCollisionWith
import raylib.core.loadTexture
import raylib.core.mutableStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.remember
import raylib.core.setValue

private const val itemsBaseDictionary = "resources/TowDSampleRes/Items/Fruits"

enum class CollectionItem(val fileName: String) {
    APPLE("Apple.png"),
    BANANAS("Bananas.png"),
}

private fun CollectionItem.grid() = when (this) {
    CollectionItem.APPLE -> 17 to 1
    CollectionItem.BANANAS -> 17 to 1
}

private const val itemSize = 50f

fun ComponentRegistry.collectionItem(
    item: CollectionItem,
    positions: List<CValue<Vector2>>,
) = component("collectionItem_$item") {
    val itemTexture = remember {
        loadTexture("$itemsBaseDictionary/${item.fileName}")
    }
    val collectedTexture = remember {
        loadTexture("$itemsBaseDictionary/Collected.png")
    }
    positions.forEachIndexed { index, position ->
        val (positionX, positionY) = position.useContents { x to y }
        val offset = itemSize.div(2f)
        item(
            "$index",
            itemTexture = itemTexture,
            collectedTexture = collectedTexture,
            grid = item.grid(),
            position = position,
            dst = Rectangle(positionX - offset, positionY - offset, itemSize, itemSize),
        )
    }
}

private fun ComponentRegistry.item(
    tag: String,
    itemTexture: CValue<Texture>,
    collectedTexture: CValue<Texture>,
    grid: SpriteGrid,
    position: CValue<Vector2>,
    dst: CValue<Rectangle>,
) = component(tag) {
    var isCollected by remember {
        mutableStateOf(false)
    }
    var isDisappear by remember {
        mutableStateOf(false)
    }

    if (!isDisappear) {
        onUpdate {
            val playerHitbox = find<PlayerHitboxContext>().hitbox
            if (position.isCollisionWith(playerHitbox)) {
                isCollected = true
            }
        }
        onDraw {
            drawRectangle(dst, Colors.BROWN)
        }

        component(tag) {
            if (!isCollected) {
                spriteAnimationComponent(
                    tag ="item",
                    texture = itemTexture,
                    spriteGrid = grid,
                    framesSpeed = mutableStateOf(12),
                    dest = dst,
                )
            } else {
                spriteAnimationComponent(
                    tag = "disappear",
                    texture = collectedTexture,
                    spriteGrid = 6 to 1,
                    framesSpeed = mutableStateOf(12),
                    dest = dst,
                    onRestart = {
                        isDisappear = true
                    }
                )
            }
        }
    }
}
