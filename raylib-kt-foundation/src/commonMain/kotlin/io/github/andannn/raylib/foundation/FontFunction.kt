/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.foundation

import kotlinx.cinterop.*
import raylib.interop.ExportFontAsCode
import raylib.interop.GenImageFontAtlas
import raylib.interop.GetFontDefault
import raylib.interop.IsFontValid
import raylib.interop.LoadFont
import raylib.interop.LoadFontData
import raylib.interop.LoadFontEx
import raylib.interop.LoadFontFromImage
import raylib.interop.LoadFontFromMemory
import raylib.interop.UnloadFont
import raylib.interop.UnloadFontData

interface FontFunction {
    fun getFontDefault(): CValue<Font>
    fun loadFont(fileName: String): CValue<Font>
    fun loadFontEx(fileName: String, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font>
    fun loadFontFromImage(image: CValue<Image>, key: CValue<Color>, firstChar: Int): CValue<Font>
    fun loadFontFromMemory(fileType: String, fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font>
    fun unloadFont(font: CValue<Font>)

    fun isFontValid(font: CValue<Font>): Boolean

    fun loadFontData(fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int, type: Int, glyphCount: CPointer<IntVar>): CPointer<GlyphInfo>?
    fun unloadFontData(glyphs: CPointer<GlyphInfo>, glyphCount: Int)

    fun genImageFontAtlas(glyphs: CPointer<GlyphInfo>, glyphRecs: CPointer<CPointerVar<Rectangle>>, glyphCount: Int, fontSize: Int, padding: Int, packMethod: Int): CValue<Image>

    fun exportFontAsCode(font: CValue<Font>, fileName: String): Boolean
}

fun FontFunction() : FontFunction = DefaultFontFunction()

private class DefaultFontFunction : FontFunction {
    override fun getFontDefault(): CValue<Font> = GetFontDefault()

    override fun loadFont(fileName: String): CValue<Font> = LoadFont(fileName)

    override fun loadFontEx(fileName: String, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font> {
        return LoadFontEx(fileName, fontSize, codepoints, codepointCount)
    }

    override fun loadFontFromImage(image: CValue<Image>, key: CValue<Color>, firstChar: Int): CValue<Font> {
        return LoadFontFromImage(image, key, firstChar)
    }

    override fun loadFontFromMemory(fileType: String, fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int): CValue<Font> {
        return LoadFontFromMemory(fileType, fileData, dataSize, fontSize, codepoints, codepointCount)
    }

    override fun isFontValid(font: CValue<Font>): Boolean = IsFontValid(font)

    override fun loadFontData(fileData: CPointer<UByteVar>, dataSize: Int, fontSize: Int, codepoints: CPointer<IntVar>?, codepointCount: Int, type: Int, glyphCount: CPointer<IntVar>): CPointer<GlyphInfo>? {
        return LoadFontData(fileData, dataSize, fontSize, codepoints, codepointCount, type, glyphCount)
    }

    override fun genImageFontAtlas(glyphs: CPointer<GlyphInfo>, glyphRecs: CPointer<CPointerVar<Rectangle>>, glyphCount: Int, fontSize: Int, padding: Int, packMethod: Int): CValue<Image> {
        return GenImageFontAtlas(glyphs, glyphRecs, glyphCount, fontSize, padding, packMethod)
    }

    override fun unloadFontData(glyphs: CPointer<GlyphInfo>, glyphCount: Int) {
        UnloadFontData(glyphs, glyphCount)
    }

    override fun unloadFont(font: CValue<Font>) {
        UnloadFont(font)
    }

    override fun exportFontAsCode(font: CValue<Font>, fileName: String): Boolean {
        return ExportFontAsCode(font, fileName)
    }
}