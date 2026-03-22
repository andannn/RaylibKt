/*
 * Copyright 2026, the RaylibKt project contributors
 * SPDX-License-Identifier: Zlib
 */
package io.github.andannn.raylib.rres

import rres.interop.rresCentralDir
import rres.interop.rresCodeLang
import rres.interop.rresCompressionType
import rres.interop.rresDirEntry
import rres.interop.rresEncryptionType
import rres.interop.rresErrorType
import rres.interop.rresFileHeader
import rres.interop.rresFontGlyphInfo
import rres.interop.rresFontStyle
import rres.interop.rresPixelFormat
import rres.interop.rresResourceChunk
import rres.interop.rresResourceChunkData
import rres.interop.rresResourceChunkInfo
import rres.interop.rresResourceDataType
import rres.interop.rresResourceMulti
import rres.interop.rresTextEncoding
import rres.interop.rresVertexAttribute
import rres.interop.rresVertexFormat

typealias ResourceChunk = rresResourceChunk
typealias FileHeader = rresFileHeader
typealias ResourceChunkInfo = rresResourceChunkInfo
typealias ResourceChunkData = rresResourceChunkData
typealias ResourceMulti = rresResourceMulti
typealias DirEntry = rresDirEntry
typealias CentralDir = rresCentralDir
typealias FontGlyphInfo = rresFontGlyphInfo

typealias ResourceDataType = rresResourceDataType
typealias CompressionType = rresCompressionType
typealias EncryptionType = rresEncryptionType
typealias ErrorType = rresErrorType
typealias TextEncoding = rresTextEncoding
typealias CodeLang = rresCodeLang
typealias PixelFormat = rresPixelFormat
typealias VertexAttribute = rresVertexAttribute
typealias VertexFormat = rresVertexFormat
typealias FontStyle = rresFontStyle
