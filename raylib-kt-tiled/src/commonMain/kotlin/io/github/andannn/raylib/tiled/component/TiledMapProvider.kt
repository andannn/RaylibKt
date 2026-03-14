package io.github.andannn.raylib.tiled.component

import io.github.andannn.raylib.tiled.model.TiledMap
import io.github.andannn.raylib.tiled.model.Tileset
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.readString
import kotlinx.serialization.json.Json

interface TiledMapFiles {
    fun tmJsonFile(): String
    fun txJsonFiles(): List<String>
}

interface TiledMapProvider {
    fun getMap(): TiledMap
}

fun TiledMapProvider(
    tmJsonFile: String,
    txJsonFileDictionary: String = Path(tmJsonFile).parent.toString()
): TiledMapProvider = TiledMapProviderImpl(
    object : TiledMapFiles {
        override fun tmJsonFile(): String {
            return tmJsonFile
        }

        override fun txJsonFiles(): List<String> {
            return SystemFileSystem.list(Path(txJsonFileDictionary))
                .filter { it.name.endsWith("tsj") }
                .map { it.toString() }
        }
    }
)

fun TiledMapProvider(files: TiledMapFiles): TiledMapProvider = TiledMapProviderImpl(files)

private class TiledMapProviderImpl(
    private val files: TiledMapFiles,
) : TiledMapProvider {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    override fun getMap(): TiledMap {
        val tmJson = SystemFileSystem.source(Path(files.tmJsonFile())).buffered().readString()
        val map = Json.decodeFromString<TiledMap>(tmJson)
        val txJsonFiles = files.txJsonFiles()

        val tilesets = map.tilesets.map { tileset ->
            txJsonFiles.firstOrNull { path ->
                Path(path).name == tileset.source
            }?.let { matchedFile ->
                val tsXJson = SystemFileSystem.source(Path(matchedFile)).buffered().readString()
                val matchedExternalTileset = json.decodeFromString<Tileset>(tsXJson)
                val imagePath = Path(tileset.image ?: matchedExternalTileset.image ?: error("Image must be set."))
                val parentPath = Path(matchedFile).parent!!
                val absoluteImagePath = imagePath.takeIf { it.isAbsolute } ?: Path(parentPath, imagePath.toString())
                tileset.copy(
                    backgroundColor = tileset.backgroundColor ?: matchedExternalTileset.backgroundColor,
                    className = tileset.className ?: matchedExternalTileset.className,
                    columns = tileset.columns ?: matchedExternalTileset.columns,
                    fillMode = matchedExternalTileset.fillMode,
                    image = absoluteImagePath.toString(),
                    imageHeight = tileset.imageHeight ?: matchedExternalTileset.imageHeight,
                    imageWidth = tileset.imageWidth ?: matchedExternalTileset.imageWidth,
                    margin = if (tileset.margin != 0) tileset.margin else matchedExternalTileset.margin,
                    name = tileset.name ?: matchedExternalTileset.name,
                    objectAlignment = matchedExternalTileset.objectAlignment,
                    spacing = if (tileset.spacing != 0) tileset.spacing else matchedExternalTileset.spacing,
                    tileCount = tileset.tileCount ?: matchedExternalTileset.tileCount,
                    tileHeight = tileset.tileHeight ?: matchedExternalTileset.tileHeight,
                    tileRenderSize = matchedExternalTileset.tileRenderSize,
                    tileWidth = tileset.tileWidth ?: matchedExternalTileset.tileWidth,
                    transparentColor = tileset.transparentColor ?: matchedExternalTileset.transparentColor,
                )
            } ?: tileset
        }

        return map.copy(
            tilesets = tilesets
        )
    }
}
