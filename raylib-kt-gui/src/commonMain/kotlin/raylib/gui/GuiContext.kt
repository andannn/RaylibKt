package raylib.gui

import raylib.core.ComponentScope
import raylib.core.Context
import raylib.core.ContextRegistry
import raylib.core.DrawContext
import raylib.core.find
import raylib.core.onDraw

fun ComponentScope.onDrawGui(block: GuiContext.() -> Unit) {
    onDraw {
        with(find<GuiContext>()) {
            block()
        }
    }
}

interface GuiContext : Context, GuiFunction, DrawContext

fun ContextRegistry.GuiContext(): GuiContext =
    object : GuiContext, GuiFunction by GuiFunction(), DrawContext by find<DrawContext>() {}
