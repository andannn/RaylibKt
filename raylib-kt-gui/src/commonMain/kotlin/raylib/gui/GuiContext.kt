package raylib.gui

import raylib.core.ComponentScope
import raylib.core.ContextRegistry
import raylib.core.DrawContext
import raylib.core.get

fun ComponentScope.onDrawGui(block: GuiContext.() -> Unit) {
    onDraw {
        with(get<GuiContext>()) {
            block()
        }
    }
}


interface GuiContext : GuiFunction, DrawContext

fun ContextRegistry.GuiContext(): GuiContext =
    object : GuiContext, GuiFunction by GuiFunction(), DrawContext by get<DrawContext>() {}
