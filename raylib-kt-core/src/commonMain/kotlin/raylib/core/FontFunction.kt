/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package raylib.core

import kotlinx.cinterop.*

interface FontFunction {
    // --- 字体加载与卸载 ---
    fun getFontDefault(): CValue<Font>
    fun loadFont(fileName: String): CValue<Font>
    fun loadFontEx(fileName: String, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font>
    fun loadFontFromImage(image: CValue<Image>, key: CValue<Color>, firstChar: Int): CValue<Font>
    fun loadFontFromMemory(fileType: String, fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font>
    fun unloadFont(font: CValue<Font>)

    // --- 状态检查 ---
    fun isFontValid(font: CValue<Font>): Boolean

    // --- 底层字形数据操作 (RAM) ---
    fun loadFontData(fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int, type: Int, glyphCount: CPointer<IntVar>): CPointer<GlyphInfo>?
    fun unloadFontData(glyphs: CPointer<GlyphInfo>, glyphCount: Int)

    // --- 字体图集生成 ---
    // 注意：Rectangle** 在 Kotlin 中映射为 CPointer<CPointerVar<Rectangle>>
    fun genImageFontAtlas(glyphs: CPointer<GlyphInfo>, glyphRecs: CPointer<CPointerVar<Rectangle>>, glyphCount: Int, fontSize: Int, padding: Int, packMethod: Int): CValue<Image>

    // --- 导出 ---
    fun exportFontAsCode(font: CValue<Font>, fileName: String): Boolean
}

fun FontFunction() : FontFunction = DefaultFontFunction()

private class DefaultFontFunction : FontFunction {
    override fun getFontDefault(): CValue<Font> = raylib.interop.GetFontDefault()

    override fun loadFont(fileName: String): CValue<Font> = raylib.interop.LoadFont(fileName)

    override fun loadFontEx(fileName: String, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font> {
        return raylib.interop.LoadFontEx(fileName, fontSize, codepoints, codepointCount)
    }

    override fun loadFontFromImage(image: CValue<Image>, key: CValue<Color>, firstChar: Int): CValue<Font> {
        return raylib.interop.LoadFontFromImage(image, key, firstChar)
    }

    override fun loadFontFromMemory(fileType: String, fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font> {
        return raylib.interop.LoadFontFromMemory(fileType, fileData, dataSize, fontSize, codepoints, codepointCount)
    }

    override fun isFontValid(font: CValue<Font>): Boolean = raylib.interop.IsFontValid(font)

    override fun loadFontData(fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int, type: Int, glyphCount: CPointer<IntVar>): CPointer<GlyphInfo>? {
        return raylib.interop.LoadFontData(fileData, dataSize, fontSize, codepoints, codepointCount, type, glyphCount)
    }

    override fun genImageFontAtlas(glyphs: CPointer<GlyphInfo>, glyphRecs: CPointer<CPointerVar<Rectangle>>, glyphCount: Int, fontSize: Int, padding: Int, packMethod: Int): CValue<Image> {
        return raylib.interop.GenImageFontAtlas(glyphs, glyphRecs, glyphCount, fontSize, padding, packMethod)
    }

    override fun unloadFontData(glyphs: CPointer<GlyphInfo>, glyphCount: Int) {
        raylib.interop.UnloadFontData(glyphs, glyphCount)
    }

    override fun unloadFont(font: CValue<Font>) {
        raylib.interop.UnloadFont(font)
    }

    override fun exportFontAsCode(font: CValue<Font>, fileName: String): Boolean {
        return raylib.interop.ExportFontAsCode(font, fileName)
    }
}