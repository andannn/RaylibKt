package me.raylib.sample.custom.game

import kotlinx.cinterop.CValue
import raylib.core.Colors.LIGHTGRAY
import raylib.core.ComponentRegistry
import raylib.core.Context
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.component
import raylib.core.components.Transform2DAlloc
import raylib.core.components.getHitbox
import raylib.core.components.transform2DComponent
import raylib.core.mutableStateOf
import raylib.core.onDraw
import raylib.core.provide
import raylib.core.remember
import raylib.core.rlMatrix

private const val characterWidth = 50f
private const val characterHeight = 50f

private val appleCollectionItems = listOf(
    Vector2(50f, 250f),
    Vector2(50f, 300f),
    Vector2(50f, 350f),
    Vector2(100f, 300f),
    Vector2(100f, 350f),
    Vector2(150f, 350f),
)

fun ComponentRegistry.twoDGameSample() {
    component("2D Game") {
        val state = remember {
            mutableStateOf(MainCharacterState.IDLE)
        }
        val transform = remember {
            Transform2DAlloc(
                position = Vector2(400f, 200f),
                offset = Vector2(-characterWidth / 2f, -characterHeight)
            )
        }
        val playerHitboxContext = remember {
            PlayerHitboxContext()
        }

        val collisionBlocks = remember {
            listOf(
                Rectangle(0f, 400f, 800f, 50f)
            )
        }

        background(Background.Brown)
        characterControl(transform, state, collisionBlocks)

        onDraw {
            rlMatrix {
                translate(0f, 25 * 50f, 0f)
                rotate(90f, 1f, 0f, 0f)
                drawGrid(100, 50f)
            }
        }

        onDraw {
            collisionBlocks.forEach { block ->
                drawRectangle(block, LIGHTGRAY)
            }
        }

        playerHitboxContext.internalRectangle = transform.getHitbox(characterWidth.times(0.9f), characterHeight.times(0.9f))

        provide(playerHitboxContext) {
            collectionItem(
                CollectionItem.APPLE,
                appleCollectionItems
            )

            transform2DComponent(transform, tag = "player") {
                mainCharacterSpritAnimation(
                    mainCharacter = MainCharacter.VIRTUAL_GUY,
                    width = characterWidth,
                    height = characterHeight,
                    state = state
                )
            }
        }
    }
}

class PlayerHitboxContext: Context {
    internal var internalRectangle: CValue<Rectangle>? = null

    val hitbox get() = internalRectangle!!
}