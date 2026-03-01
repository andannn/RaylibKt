package raylib.easings

import raylib.core.SuspendingUpdateEventScope
import kotlin.time.Duration
import kotlin.time.DurationUnit

suspend fun SuspendingUpdateEventScope.awaitEasingAnimation(
    start: Float,
    target: Float,
    duration: Duration,
    easing: Easing = Ease.LinearNone,
    onUpdate: (Float) -> Unit
) = awaitDuration(duration) { fraction ->
    onUpdate(start.animateTo(target, fraction, easing))
}

suspend fun SuspendingUpdateEventScope.awaitDuration(
    duration: Duration,
    onProgress: (Float) -> Unit = {}
) = awaitUpdateEventScope {
    val totalSeconds = duration.toDouble(DurationUnit.SECONDS).toFloat()
    if (totalSeconds <= 0f) {
        onProgress(1f)
        return@awaitUpdateEventScope
    }

    var elapsedTime = 0f

    while (true) {
        val dt = awaitUpdateEvent()
        elapsedTime += dt

        val fraction = (elapsedTime / totalSeconds).coerceIn(0f, 1f)

        onProgress(fraction)

        if (fraction >= 1f) break
    }
}

