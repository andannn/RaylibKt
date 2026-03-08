package raylib.core

interface WindowContext : Context, WindowFunction, ImageFunction , FontFunction {
    var renderPhase: RenderPhase
}

fun WindowContext(
    windowFunction: WindowFunction,
    fontFunction: FontFunction = FontFunction(),
    imageFunction: ImageFunction = ImageFunction(),
): WindowContext = WindowContextImpl(
    windowFunction,
    imageFunction,
    fontFunction
)

internal class WindowContextImpl(
    windowFunction: WindowFunction,
    imageFunction: ImageFunction,
    fontFunction: FontFunction,
    ) : WindowContext,
    WindowFunction by windowFunction,
    FontFunction by fontFunction,
    ImageFunction by imageFunction {
    override var renderPhase = RenderPhase.UPDATE
}
