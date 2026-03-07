package raylib.core

interface WindowContext : Context, WindowFunction {
    var renderPhase: RenderPhase
}

fun WindowContext(
    windowFunction: WindowFunction,
): WindowContext = WindowContextImpl(
    windowFunction
)

internal class WindowContextImpl(
    windowFunction: WindowFunction,
) : WindowContext,
    WindowFunction by windowFunction {
    override var renderPhase = RenderPhase.UPDATE
}
