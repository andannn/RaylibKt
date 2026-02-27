package me.raylib.sample.core

import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import raylib.core.Colors.BLUE
import raylib.core.Colors.GRAY
import raylib.core.Colors.GREEN
import raylib.core.Colors.RED
import raylib.core.Colors.WHITE
import raylib.core.GameContext
import raylib.core.KeyboardKey
import raylib.core.Vector2
import raylib.core.stateOf
import raylib.core.window
import raylib.interop.GamepadButton

private enum class ActionType {
    NO_ACTION,
    ACTION_UP,
    ACTION_DOWN,
    ACTION_LEFT,
    ACTION_RIGHT,
    ACTION_FIRE,
    MAX_ACTION
}

data class ActionInput(
    val key: KeyboardKey,
    val button: GamepadButton,
)

private val defaultKeySet: Map<ActionType, ActionInput> = mapOf(
    ActionType.ACTION_UP to ActionInput(KeyboardKey.KEY_W, GamepadButton.GAMEPAD_BUTTON_LEFT_FACE_UP),
    ActionType.ACTION_DOWN to ActionInput(KeyboardKey.KEY_S, GamepadButton.GAMEPAD_BUTTON_LEFT_FACE_DOWN),
    ActionType.ACTION_LEFT to ActionInput(KeyboardKey.KEY_A, GamepadButton.GAMEPAD_BUTTON_LEFT_FACE_LEFT),
    ActionType.ACTION_RIGHT to ActionInput(KeyboardKey.KEY_D, GamepadButton.GAMEPAD_BUTTON_LEFT_FACE_RIGHT),
    ActionType.ACTION_FIRE to ActionInput(KeyboardKey.KEY_SPACE, GamepadButton.GAMEPAD_BUTTON_RIGHT_FACE_DOWN),
)

private val alternateKeySet: Map<ActionType, ActionInput> = mapOf(
    ActionType.ACTION_UP to ActionInput(KeyboardKey.KEY_UP, GamepadButton.GAMEPAD_BUTTON_RIGHT_FACE_UP),
    ActionType.ACTION_DOWN to ActionInput(KeyboardKey.KEY_DOWN, GamepadButton.GAMEPAD_BUTTON_RIGHT_FACE_DOWN),
    ActionType.ACTION_LEFT to ActionInput(KeyboardKey.KEY_LEFT, GamepadButton.GAMEPAD_BUTTON_RIGHT_FACE_LEFT),
    ActionType.ACTION_RIGHT to ActionInput(KeyboardKey.KEY_RIGHT, GamepadButton.GAMEPAD_BUTTON_RIGHT_FACE_RIGHT),
    ActionType.ACTION_FIRE to ActionInput(KeyboardKey.KEY_SPACE, GamepadButton.GAMEPAD_BUTTON_RIGHT_FACE_DOWN),
)

private var currentKeySet = defaultKeySet

private fun GameContext.isDown(type: ActionType) =
    currentKeySet[type]!!.key.isDown() || currentKeySet[type]!!.button.isDown(0)

private fun GameContext.isPressed(type: ActionType) =
    currentKeySet[type]!!.key.isPressed() || currentKeySet[type]!!.button.isPressed(0)

private fun GameContext.isReleased(type: ActionType) =
    currentKeySet[type]!!.key.isReleased() || currentKeySet[type]!!.button.isReleased(0)

fun inputActions() {
    window(
        title = "raylib [core] example - input actions",
        width = 800,
        height = 450,
        initialBackGroundColor = GRAY
    ) {
        componentRegistry {
            component("key") {
                val position: Vector2 by stateOf { alloc { x = 400.0f; y = 200.0f } }
                val size: Vector2 by stateOf { alloc { x = 40.0f; y = 40.0f } }
                var actionSet = false
                var releaseAction = false
                provideHandlers {
                    onUpdate {
                        if (isDown(ActionType.ACTION_UP)) position.y -= 2f
                        if (isDown(ActionType.ACTION_DOWN)) position.y += 2f
                        if (isDown(ActionType.ACTION_LEFT)) position.x -= 2f
                        if (isDown(ActionType.ACTION_RIGHT)) position.x += 2f
                        if (isPressed(ActionType.ACTION_FIRE)) {
                            position.x = (screenWidth - size.x) / 2;
                            position.y = (screenHeight - size.y) / 2;
                        }

                        releaseAction = false
                        if (isReleased(ActionType.ACTION_FIRE)) releaseAction = true

                        if (KeyboardKey.KEY_TAB.isPressed()) {
                            actionSet = !actionSet
                            if (actionSet) currentKeySet = defaultKeySet
                            else currentKeySet = alternateKeySet
                        }
                    }
                    onDraw {
                        drawRectangle(position.readValue(), size.readValue(), if (releaseAction) BLUE else RED)
                        drawText(
                            if (actionSet) "Current input set: WASD (default)" else "Current input set: Arrow keys",
                            10,
                            10,
                            20,
                            WHITE
                        )
                        drawText("Use TAB key to toggles Actions keyset", 10, 50, 20, GREEN)
                    }
                }
            }
        }
    }
}