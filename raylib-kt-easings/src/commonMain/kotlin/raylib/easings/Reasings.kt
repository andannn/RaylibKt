package raylib.easings

import reasings.interop.*

fun interface Easing {
    fun transform(fraction: Float): Float
}

fun Float.lerp(target: Float, fraction: Float): Float {
    return this + (target - this) * fraction
}

fun Float.animateTo(
    target: Float,
    fraction: Float,
    easing: Easing = Ease.LinearNone
): Float {
    val clampedFraction = fraction.coerceIn(0f, 1f)
    val easedFraction = easing.transform(clampedFraction)
    return this.lerp(target, easedFraction)
}

object Ease {
    // Linear
    val LinearNone = Easing { t -> EaseLinearNone(t, 0f, 1f, 1f) }
    val LinearIn = Easing { t -> EaseLinearIn(t, 0f, 1f, 1f) }
    val LinearOut = Easing { t -> EaseLinearOut(t, 0f, 1f, 1f) }
    val LinearInOut = Easing { t -> EaseLinearInOut(t, 0f, 1f, 1f) }

    // Sine
    val SineIn = Easing { t -> EaseSineIn(t, 0f, 1f, 1f) }
    val SineOut = Easing { t -> EaseSineOut(t, 0f, 1f, 1f) }
    val SineInOut = Easing { t -> EaseSineInOut(t, 0f, 1f, 1f) }

    // Quad
    val QuadIn = Easing { t -> EaseQuadIn(t, 0f, 1f, 1f) }
    val QuadOut = Easing { t -> EaseQuadOut(t, 0f, 1f, 1f) }
    val QuadInOut = Easing { t -> EaseQuadInOut(t, 0f, 1f, 1f) }

    // Cubic
    val CubicIn = Easing { t -> EaseCubicIn(t, 0f, 1f, 1f) }
    val CubicOut = Easing { t -> EaseCubicOut(t, 0f, 1f, 1f) }
    val CubicInOut = Easing { t -> EaseCubicInOut(t, 0f, 1f, 1f) }

    // Expo
    val ExpoIn = Easing { t -> EaseExpoIn(t, 0f, 1f, 1f) }
    val ExpoOut = Easing { t -> EaseExpoOut(t, 0f, 1f, 1f) }
    val ExpoInOut = Easing { t -> EaseExpoInOut(t, 0f, 1f, 1f) }

    // Circ
    val CircIn = Easing { t -> EaseCircIn(t, 0f, 1f, 1f) }
    val CircOut = Easing { t -> EaseCircOut(t, 0f, 1f, 1f) }
    val CircInOut = Easing { t -> EaseCircInOut(t, 0f, 1f, 1f) }

    // Back
    val BackIn = Easing { t -> EaseBackIn(t, 0f, 1f, 1f) }
    val BackOut = Easing { t -> EaseBackOut(t, 0f, 1f, 1f) }
    val BackInOut = Easing { t -> EaseBackInOut(t, 0f, 1f, 1f) }

    // Elastic
    val ElasticIn = Easing { t -> EaseElasticIn(t, 0f, 1f, 1f) }
    val ElasticOut = Easing { t -> EaseElasticOut(t, 0f, 1f, 1f) }
    val ElasticInOut = Easing { t -> EaseElasticInOut(t, 0f, 1f, 1f) }

    // Bounce
    val BounceIn = Easing { t -> EaseBounceIn(t, 0f, 1f, 1f) }
    val BounceOut = Easing { t -> EaseBounceOut(t, 0f, 1f, 1f) }
    val BounceInOut = Easing { t -> EaseBounceInOut(t, 0f, 1f, 1f) }
}

