package raylib.core

interface WindowContext : Context, WindowFunction

fun WindowContext(
    windowFunction: WindowFunction,
): WindowContext = WindowContextImpl(
    windowFunction
)

internal class WindowContextImpl(
    windowFunction: WindowFunction,
) : WindowContext,
    WindowFunction by windowFunction
