package io.github.andannn.raylib.tiled.model

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class JsonParseTest {
    @Test
    fun jsonParseTest_parse_map() {
        val decoded = Json.decodeFromString<TiledMap>(
            """
            { "compressionlevel":4,
             "height":20,
             "infinite":false,
             "layers":[],
             "nextlayerid":5,
             "nextobjectid":2,
             "orientation":"orthogonal",
             "renderorder":"right-down",
             "tiledversion":"1.12.0",
             "tileheight":16,
             "tilesets":[
                    {
                     "firstgid":1,
                     "source":"Confetti (16x16).tsj"
                    }, 
                    {
                     "firstgid":7,
                     "source":"Terrain (16x16).tsj"
                    }],
             "tilewidth":16,
             "type":"map",
             "version":"1.10",
             "width":30
            }
        """.trimIndent()
        )
        assertEquals(30, decoded.width)
        assertEquals(20, decoded.height)
        assertEquals(16, decoded.tileHeight)
        assertEquals(16, decoded.tileWidth)
    }

    @Test
    fun jsonParseTest_parse_tiled_layer() {
        val decode = Json.decodeFromString<List<Layer>>(
            """
[
  {
    "id": 8,
    "layers": [
      {
        "draworder": "topdown",
        "id": 9,
        "name": "ObjectLayer",
        "objects": [
          {
            "height": 27.5904353157572,
            "id": 3,
            "name": "Object1",
            "opacity": 1,
            "rotation": 0,
            "type": "",
            "visible": true,
            "width": 30.0429184549356,
            "x": 28.8166768853464,
            "y": 20.0286123032904
          }
        ],
        "opacity": 1,
        "type": "objectgroup",
        "visible": true,
        "x": 0,
        "y": 0
      },
      {
        "compression": "gzip",
        "data": "H4sIAAAAAAAAE2NgGAWjAD9ghOKBANS2G2YePgwD6HxKAKnmkKoeGyDH\/aSqxwZINYNU9bgAsf4lVh2pAF8aQhenBaCXPaNgFIwC4gAAmas7JmAJAAA=",
        "encoding": "base64",
        "height": 20,
        "id": 5,
        "name": "Tile2",
        "opacity": 1,
        "type": "tilelayer",
        "visible": true,
        "width": 30,
        "x": 0,
        "y": 0
      },
      {
        "compression": "gzip",
        "data": "H4sIAAAAAAAAE+3NIQ4AAAjDQML\/H00QMwS9mZ6saRUA6RsM9pn6JvD1SH55e+md+uM3w8IHdWAJAAA=",
        "encoding": "base64",
        "height": 20,
        "id": 6,
        "name": "Tile1",
        "opacity": 1,
        "type": "tilelayer",
        "visible": true,
        "width": 30,
        "x": 0,
        "y": 0
      }
    ],
    "name": "Group",
    "opacity": 1,
    "type": "group",
    "visible": true,
    "x": 0,
    "y": 0
  },
  {
    "compression": "gzip",
    "data": "H4sIAAAAAAAAE+3PSwrAMAgE0FDo\/a9cXASGQfFTwUXyIAttOtOulfM4czfJtzqs\/V9erve8KpLLd3jOir4fvYdeOIzzeN6sfRT2a1naTlj7DO\/\/uUPbVUkO9uM37B48XTqzMiZ6JzrFab1isvu6qj7TbopcYAkAAA==",
    "encoding": "base64",
    "height": 20,
    "id": 10,
    "name": "Tile3",
    "opacity": 1,
    "type": "tilelayer",
    "visible": true,
    "width": 30,
    "x": 0,
    "y": 0
  },
  {
    "draworder": "topdown",
    "id": 11,
    "name": "ObjectLayerOutSide",
    "objects": [
      {
        "height": 30.0429184549356,
        "id": 6,
        "name": "Object2",
        "opacity": 1,
        "rotation": 0,
        "type": "",
        "visible": true,
        "width": 34.3347639484979,
        "x": 94.6249744533006,
        "y": 17.9848763539751
      },
      {
        "height": 0,
        "id": 7,
        "name": "",
        "opacity": 1,
        "rotation": 0,
        "type": "",
        "visible": true,
        "width": 0.408747189863064,
        "x": 50.071530758226,
        "y": -19.2111179235643
      }
    ],
    "opacity": 1,
    "type": "objectgroup",
    "visible": true,
    "x": 0,
    "y": 0
  }
]
        """.trimIndent()
        )
        val layer = assertIs<GroupLayer>(decode.first())
        assertEquals(3, layer.layers.size)
        val child1 = assertIs<ObjectGroupLayer>(layer.layers.first()).also {
            assertEquals("ObjectLayer", it.name)
            assertEquals("Object1", it.objects[0].name)
            assertEquals(30.0429184549356, it.objects[0].width)
            assertEquals(28.8166768853464, it.objects[0].x)
        }

        val child2 = assertIs<TileLayer>(layer.layers[1])
        val child3 = assertIs<TileLayer>(layer.layers[2])
        println("${child3.data}")
    }

    @Test
    fun jsonParseTest_parse_csv_layer_data() {
        val decode = Json.decodeFromString<TiledMap>(
            """
            { "compressionlevel":4,
             "height":20,
             "infinite":false,
             "layers":[
                    {
                     "data":[2684354570, 10, 1610612746, 3221225482, 1073741834, 10, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
                        0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0],
                     "height":20,
                     "id":12,
                     "name":"Tile Layer 1",
                     "opacity":1,
                     "type":"tilelayer",
                     "visible":true,
                     "width":30,
                     "x":0,
                     "y":0
                    }],
             "nextlayerid":13,
             "nextobjectid":8,
             "orientation":"orthogonal",
             "renderorder":"right-down",
             "tiledversion":"1.12.0",
             "tileheight":16,
             "tilesets":[
                    {
                     "firstgid":1,
                     "source":"Confetti (16x16).tsj"
                    }, 
                    {
                     "firstgid":7,
                     "source":"Terrain (16x16).tsj"
                    }],
             "tilewidth":16,
             "type":"map",
             "version":"1.10",
             "width":30
            }
        """.trimIndent()
        )

        val tileLayer = assertIs<TileLayer>(decode.layers[0])
        tileLayer.gidArray.also {
            it[0].also {
                assertEquals(10, it.tileId)
                assertEquals(true, it.flipHorizontal)
            }
            it[1].also {
                assertEquals(10, it.tileId)
                assertEquals(false, it.flipHorizontal)
            }
            it[2].also {
                println(it)
                assertEquals(10, it.tileId)
                assertEquals(false, it.flipHorizontal)
                assertEquals(true, it.flipDiagonal)
                assertEquals(true, it.flipVertical)
            }
        }
    }
}
