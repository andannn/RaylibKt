package me.raylib.sample.custom.game

import kotlinx.cinterop.CValue
import kotlinx.cinterop.useContents
import raylib.core.Colors.RED
import raylib.core.ComponentRegistry
import raylib.core.KeyboardKey
import raylib.core.MutableState
import raylib.core.Rectangle
import raylib.core.State
import raylib.core.Vector2Alloc
import raylib.core.component
import raylib.core.components.Transform2D
import raylib.core.components.spriteAnimationComponent
import raylib.core.getValue
import raylib.core.loadTexture
import raylib.core.mutableStateOf
import raylib.core.nativeStateOf
import raylib.core.onDraw
import raylib.core.onUpdate
import raylib.core.remember
import raylib.core.setValue

private const val horizontalMoveSpeed = 250f
private const val jumpSpeed = 600f
private const val doubleJumpSpeed = jumpSpeed * 0.8f
private const val G = 1800

fun ComponentRegistry.characterControl(
    transform: Transform2D,
    state: MutableState<MainCharacterState>,
    collisionBlocks: List<CValue<Rectangle>>
) = component("character control") {
    val speedVector by remember {
        nativeStateOf { Vector2Alloc() }
    }
    var isOnGround by remember {
        mutableStateOf(false)
    }
    var jumpCount by remember { mutableStateOf(0) }
    val maxJumps = 2

    onUpdate { deltaTime ->
        // move
        val inputX = when {
            KeyboardKey.KEY_RIGHT.isDown() -> 1f
            KeyboardKey.KEY_LEFT.isDown() -> -1f
            else -> 0f
        }
        if (inputX != 0f) {
            transform.scale.x = inputX
        }

        speedVector.x = inputX * horizontalMoveSpeed

        // jump
        if (KeyboardKey.KEY_SPACE.isPressed()) {
            if (jumpCount < maxJumps) {
                jumpCount++
                if (jumpCount >= 2) {
                    speedVector.y = -doubleJumpSpeed
                } else {
                    speedVector.y = -jumpSpeed
                }
            }
        }
        speedVector.y += G * deltaTime

        // check collision.
        var currentHit = false
        for (rect in collisionBlocks) {
            rect.useContents {
                val rect = this
                if (rect.x <= transform.position.x &&
                    rect.x + rect.width >= transform.position.x &&
                    rect.y >= transform.position.y &&
                    rect.y <= transform.position.y + speedVector.y * deltaTime
                ) {
                    currentHit = true
                    speedVector.y = 0.0f
                    transform.position.y = rect.y
                    jumpCount = 0
                }
            }
            if (currentHit) break
        }

        isOnGround = currentHit

        // move state.
        state.value = when {
            !isOnGround -> {
                if (jumpCount >= 2) {
                    MainCharacterState.DOUBLE_JUMP
                } else {
                    if (speedVector.y >= 0) {
                        MainCharacterState.FAIL
                    } else {
                        MainCharacterState.JUMP
                    }
                }
            }

            inputX != 0f -> MainCharacterState.RUN
            else -> MainCharacterState.IDLE
        }

        transform.position.x += speedVector.x * deltaTime
        transform.position.y += speedVector.y * deltaTime
    }
}

private const val baseResDictionary = "resources/TowDSampleRes/Main Characters"

enum class MainCharacter(val resDictionary: String) {
    VIRTUAL_GUY("Virtual Guy"),
    NINJA_FROG("Ninja Frog"),
}

enum class MainCharacterState(val file: String) {
    IDLE("Idle (32x32).png"),
    RUN("Run (32x32).png"),
    JUMP("Jump (32x32).png"),
    FAIL("Fall (32x32).png"),
    DOUBLE_JUMP("Double Jump (32x32).png"),
}

fun ComponentRegistry.mainCharacterSpritAnimation(
    mainCharacter: MainCharacter,
    width: Float,
    height: Float,
    state: State<MainCharacterState>
) = component("Main Character") {
    val idleTexture =
        remember { loadTexture("$baseResDictionary/${mainCharacter.resDictionary}/${MainCharacterState.IDLE.file}") }
    val walkTexture =
        remember { loadTexture("$baseResDictionary/${mainCharacter.resDictionary}/${MainCharacterState.RUN.file}") }
    val jumpTexture =
        remember { loadTexture("$baseResDictionary/${mainCharacter.resDictionary}/${MainCharacterState.JUMP.file}") }
    val failTexture =
        remember { loadTexture("$baseResDictionary/${mainCharacter.resDictionary}/${MainCharacterState.FAIL.file}") }
    val doubleJumpTexture =
        remember { loadTexture("$baseResDictionary/${mainCharacter.resDictionary}/${MainCharacterState.DOUBLE_JUMP.file}") }
    val frameSpeed = remember { mutableStateOf(12) }
    val doubleJumpFrameSpeed = remember { mutableStateOf(18) }
    val rect = remember { Rectangle(0f, 0f, width, height) }

    onDraw {
        drawRectangle(rect, RED)
    }
    when (state.value) {
        MainCharacterState.IDLE -> spriteAnimationComponent(
            tag = "IDLE",
            texture = idleTexture,
            spriteGrid = 11 to 1,
            framesSpeed = frameSpeed,
            dest = rect
        )

        MainCharacterState.RUN -> spriteAnimationComponent(
            tag = "run",
            texture = walkTexture,
            spriteGrid = 12 to 1,
            framesSpeed = frameSpeed,
            dest = rect
        )

        MainCharacterState.JUMP -> spriteAnimationComponent(
            tag = "jump",
            texture = jumpTexture,
            spriteGrid = 1 to 1,
            framesSpeed = frameSpeed,
            dest = rect
        )

        MainCharacterState.FAIL -> spriteAnimationComponent(
            tag = "fail",
            texture = failTexture,
            spriteGrid = 1 to 1,
            framesSpeed = frameSpeed,
            dest = rect
        )

        MainCharacterState.DOUBLE_JUMP -> spriteAnimationComponent(
            tag = "double jump",
            texture = doubleJumpTexture,
            spriteGrid = 6 to 1,
            framesSpeed = doubleJumpFrameSpeed,
            dest = rect
        )
    }
}
