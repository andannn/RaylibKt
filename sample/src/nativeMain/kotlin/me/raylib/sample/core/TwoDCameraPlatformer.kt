package me.raylib.sample.core

import kotlinx.cinterop.CValue
import kotlinx.cinterop.alloc
import kotlinx.cinterop.readValue
import kotlinx.cinterop.useContents
import platform.posix.fmaxf
import raylib.core.Camera2D
import raylib.core.Color
import raylib.core.Colors
import raylib.core.Colors.BLACK
import raylib.core.Colors.DARKGRAY
import raylib.core.Colors.GOLD
import raylib.core.Colors.GRAY
import raylib.core.Colors.LIGHTGRAY
import raylib.core.Colors.RED
import raylib.core.ComponentsRegisterScope
import raylib.core.KeyboardKey
import raylib.core.MutableState
import raylib.core.Rectangle
import raylib.core.Vector2
import raylib.core.add
import raylib.core.allocRectangle
import raylib.core.length
import raylib.core.mode2d
import raylib.core.scale
import raylib.core.screenToWorldPosition
import raylib.core.mutableStateOf
import raylib.core.subtract
import raylib.core.window
import raylib.core.worldToScreenPosition

private enum class CameraOption(val description: String) {
    FollowPlayerCenter("Follow player center"),
    FollowPlayerCenterClamped("Follow player center, but clamp to map edges"),
    FollowPlayerCenterSmooth("Follow player center; smoothed"),
    FollowPlayerCenterHorizontally("Follow player center horizontally; update player center vertically after landing"),
    PlayerPushCamera("Player push camera on getting too close to screen edge");

    fun next() = entries[(ordinal + 1) % entries.size]
}

fun towDCameraPlatformer() {
    window(
        title = "raylib [core] example - raylib [core] example - 2d camera platformer",
        width = 800,
        height = 450,
        initialBackGroundColor = Colors.RAYWHITE
    ) {
        val player = Player(alloc<Vector2>().apply { x = 400f; y = 200f }, 0f, true)
        val camera = alloc<Camera2D> { zoom = 1f }
        val cameraOption = mutableStateOf(CameraOption.FollowPlayerCenter)
        val envItems = listOf(
            EnvItem(allocRectangle(0f, 0f, 1000f, 400f), false, LIGHTGRAY),
            EnvItem(allocRectangle(0f, 400f, 1000f, 200f), true, GRAY),
            EnvItem(allocRectangle(300f, 200f, 400f, 10f), true, GRAY),
            EnvItem(allocRectangle(250f, 300f, 100f, 10f), true, GRAY),
            EnvItem(allocRectangle(650f, 300f, 100f, 10f), true, GRAY),
        )

        registerComponents {
            component("changeCameraOption") {
                provideHandlers {
                    onUpdate {
                        if (KeyboardKey.KEY_C.isPressed()) {
                            cameraOption.value = cameraOption.value.next()
                        }
                    }
                }
            }
            when (cameraOption.value) {
                CameraOption.FollowPlayerCenter -> followTargetCamera(camera, player.position)
                CameraOption.FollowPlayerCenterClamped -> followTargetCenterClampedCamera(
                    camera,
                    player.position,
                    Rectangle(0f, 0f, height = 600f, width = 1000f)
                )

                CameraOption.FollowPlayerCenterSmooth -> followTargetSmoothCamera(camera, player.position)
                CameraOption.FollowPlayerCenterHorizontally -> followPlayerCenterHorizontallyCamera(camera, player)
                CameraOption.PlayerPushCamera -> playerPushCamera(camera, player.position)
            }

            envItems.forEach {
                envItemsComponent(camera, it)
            }
            playerComponent(camera, player, envItems)
            infoComponent(cameraOption)
        }
    }
}

private fun ComponentsRegisterScope.envItemsComponent(camera: Camera2D, item: EnvItem) {
    component(item) {
        provideHandlers {
            onDraw {
                mode2d(camera) {
                    drawRectangle(item.rect.readValue(), item.color)
                }
            }
        }
    }
}

const val PLAYER_HOR_SPD = 200.0f
const val PLAYER_JUMP_SPD = 350.0f
const val G = 400

private fun ComponentsRegisterScope.followTargetCenterClampedCamera(
    camera: Camera2D,
    target: Vector2,
    worldRect: CValue<Rectangle>
) {
    data class RectMinMax(
        val maxX: Float,
        val maxY: Float,
        val minX: Float,
        val minY: Float
    )
    component("followPlayerCenterClampedCamera") {
        provideHandlers {
            onUpdate {
                camera.target.x = target.x
                camera.target.y = target.y
                camera.offset.x = screenWidth / 2.0f
                camera.offset.y = screenHeight / 2.0f

                val (maxX, maxY, minX, minY) = worldRect.useContents {
                    RectMinMax(minX = x, maxX = x + width, minY = y, maxY = y + height)
                }
                val cameraValue = camera.readValue()
                val maxScreen = cameraValue.worldToScreenPosition(Vector2(maxX, maxY))
                val minScreen = cameraValue.worldToScreenPosition(Vector2(minX, minY))
                val (maxScreenX, maxScreenY) = maxScreen.useContents { x to y }
                val (minScreenX, minScreenY) = minScreen.useContents { x to y }
                if (maxScreenX < screenWidth) {
                    camera.offset.x = screenWidth - (maxScreenX - screenWidth / 2f)
                }
                if (maxScreenY < screenHeight) {
                    camera.offset.y = screenHeight - (maxScreenY - screenHeight / 2f)
                }
                if (minScreenX > 0) {
                    camera.offset.x = screenWidth / 2f - minScreenX
                }
                if (minScreenY > 0) {
                    camera.offset.y = screenHeight / 2f - minScreenY
                }
            }
        }
    }
}

private fun ComponentsRegisterScope.followTargetCamera(
    camera: Camera2D,
    position: Vector2
) {
    component("followTargetCamera") {
        provideHandlers {
            onUpdate {
                camera.offset.x = screenWidth.div(2f)
                camera.offset.y = screenHeight.div(2f)
                camera.target.x = position.x
                camera.target.y = position.y
            }
        }
    }
}

private fun ComponentsRegisterScope.followTargetSmoothCamera(
    camera: Camera2D,
    position: Vector2
) {
    component("followTargetSmooth") {
        val minSpeed = 30f
        val minEffectLength = 10;
        val fractionSpeed = 0.8f;

        provideHandlers {
            onUpdate { delta ->
                camera.offset.x = screenWidth / 2.0f
                camera.offset.y = screenHeight / 2.0f
                val diff = position.readValue().subtract(camera.target.readValue())
                val length = diff.length()
                if (length > minEffectLength) {
                    val speed: Float = fmaxf(fractionSpeed * length, minSpeed)
                    camera.target.readValue().add(diff.scale(speed * delta / length)).useContents {
                        camera.target.x = x
                        camera.target.y = y
                    }
                }
            }
        }
    }
}

private fun ComponentsRegisterScope.playerPushCamera(
    camera: Camera2D,
    position: Vector2
) {
    component("playerPushCamera") {
        provideHandlers {
            onUpdate {
                val bbox = Vector2(0.2f, 0.2f)
                val bbox_x = bbox.useContents { x }
                val bbox_y = bbox.useContents { y }
                val bboxWorldMin = camera.readValue()
                    .screenToWorldPosition(
                        Vector2(
                            (1 - bbox.useContents { x }) * 0.5f * screenWidth,
                            (1 - bbox.useContents { y }) * 0.5f * screenHeight
                        )
                    )
                val bboxWorldMax = camera.readValue()
                    .screenToWorldPosition(
                        Vector2(
                            (1 + bbox_x) * 0.5f * screenWidth,
                            (1 + bbox_y) * 0.5f * screenHeight
                        )
                    )
                Vector2((1 - bbox_x) * 0.5f * screenWidth, (1 - bbox_y) * 0.5f * screenHeight).useContents {
                    camera.offset.x = x
                    camera.offset.y = y
                }

                if (position.x < bboxWorldMin.useContents { x }) camera.target.x = position.x;
                if (position.y < bboxWorldMin.useContents { y }) camera.target.y = position.y;
                if (position.x > bboxWorldMax.useContents { x }) camera.target.x =
                    bboxWorldMin.useContents { x } + (position.x - bboxWorldMax.useContents { x })
                if (position.y > bboxWorldMax.useContents { y }) camera.target.y =
                    bboxWorldMin.useContents { y } + (position.y - bboxWorldMax.useContents { y })
            }
        }
    }
}

private fun ComponentsRegisterScope.followPlayerCenterHorizontallyCamera(
    camera: Camera2D,
    player: Player,
) {
    component("followPlayerCenterHorizontallyCamera") {
        val evenOutSpeed = 700f
        var eveningOut = false
        var evenOutTarget = 0f

        provideHandlers {
            onUpdate { delta ->
                camera.offset.x = screenWidth / 2.0f
                camera.offset.y = screenHeight / 2.0f
                camera.target.x = player.position.x
                if (eveningOut) {
                    if (evenOutTarget > camera.target.y) {
                        camera.target.y += evenOutSpeed * delta

                        if (camera.target.y > evenOutTarget) {
                            camera.target.y = evenOutTarget
                            eveningOut = false
                        }
                    } else {
                        camera.target.y -= evenOutSpeed * delta

                        if (camera.target.y < evenOutTarget) {
                            camera.target.y = evenOutTarget
                            eveningOut = false
                        }
                    }
                } else {
                    if (player.canJump && player.speed == 0f && (player.position.y != camera.target.y)) {
                        eveningOut = true
                        evenOutTarget = player.position.y
                    }
                }
            }
        }
    }
}

private fun ComponentsRegisterScope.playerComponent(camera: Camera2D, player: Player, envItem: List<EnvItem>) {
    component("player") {
        provideHandlers {
            onUpdate {
                if (KeyboardKey.KEY_R.isPressed()) {
                    camera.zoom = 1.0f
                    player.position.x = 400f
                    player.position.y = 280f
                }
            }
            onUpdate { delta ->
                if (KeyboardKey.KEY_LEFT.isDown()) {
                    player.position.x -= PLAYER_HOR_SPD * delta
                }
                if (KeyboardKey.KEY_RIGHT.isDown()) {
                    player.position.x += PLAYER_HOR_SPD * delta
                }
                if (KeyboardKey.KEY_SPACE.isDown() && player.canJump) {
                    player.speed = -PLAYER_JUMP_SPD
                    player.canJump = false
                }

                var hitObstacle = false
                for (item in envItem) {
                    val rect = item.rect
                    if (item.blocking &&
                        rect.x <= player.position.x &&
                        rect.x + rect.width >= player.position.x &&
                        rect.y >= player.position.y &&
                        rect.y <= player.position.y + player.speed * delta
                    ) {
                        hitObstacle = true
                        player.speed = 0.0f
                        player.position.y = rect.y
                        break
                    }
                }

                if (!hitObstacle) {
                    player.position.y += player.speed * delta
                    player.speed += G * delta
                    player.canJump = false
                } else {
                    player.canJump = true
                }
            }

            onDraw {
                mode2d(camera) {
                    val playerRect = Rectangle(player.position.x - 20, player.position.y - 40, 40.0f, 40.0f)
                    drawRectangle(playerRect, RED)
                    drawCircle(player.position.readValue(), 5.0f, GOLD)
                }
            }
        }
    }
}

private fun ComponentsRegisterScope.infoComponent(cameraOption: MutableState<CameraOption>) {
    component("info") {
        provideHandlers {
            onDraw {
                drawText("Controls:", 20, 20, 10, BLACK);
                drawText("- Right/Left to move", 40, 40, 10, DARKGRAY);
                drawText("- Space to jump", 40, 60, 10, DARKGRAY);
                drawText("- Mouse Wheel to Zoom in-out", 40, 80, 10, DARKGRAY);
                drawText("- R to reset position + zoom", 40, 100, 10, DARKGRAY);
                drawText("- C to change camera mode", 40, 120, 10, DARKGRAY);
                drawText("Current camera mode:", 20, 140, 10, BLACK);
                drawText(cameraOption.value.description, 40, 160, 10, DARKGRAY)
            }
        }
    }
}

private class Player(
    val position: Vector2,
    var speed: Float,
    var canJump: Boolean
)

private class EnvItem(
    val rect: Rectangle,
    var blocking: Boolean,
    val color: CValue<Color>
)
